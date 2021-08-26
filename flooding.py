import asyncio
import slixmpp
import json
import getpass
import threading
import time

#Bot que esucha mensajes entrantes, tambien es el encargado de realizar el Flooding
class ListenBot(slixmpp.ClientXMPP):
    #Inicializacion
    def __init__(self, jid, password):
        slixmpp.ClientXMPP.__init__(self, jid, password)
        self.jid
        self.add_event_handler("session_start", self.start)
        self.add_event_handler("message", self.message)
        self.register_plugin('xep_0030') # Service Discovery
        self.register_plugin('xep_0004') # Data Forms
        self.register_plugin('xep_0060') # PubSub
        self.register_plugin('xep_0199') # XMPP Ping
    
    def start(self, event):
        self.send_presence()
        self.get_roster()
        time.wait(1)

    #Al recibir un mensaje le hacce fordwar a sus vecinos, teniendo en cuenta la cantidad de saltos, y aquienes ya les ha llegado el mensaje
    def message(self, msg):
        if msg['type'] in ('chat', 'normal'):
            #Extraccion de datos del mensaje (payload)
            sender = str(msg['from'])
            jidS = sender.split('/')[0]
            payload = msg['body'].split("./.")
            ufrom = payload[0]
            dest = payload[1]
            jumps = int(payload[2])
            jumps -= 1
            visited = payload[3]
            the_msg = payload[4]

            
            forward = []
            # En caso de no ser el nodo destino realizar fordward
            if dest != self.jid:
                #Se determina a quienes si se hara fordward
                key = get_key(identificadores, self.jid)
                neighbors = topologia[key]
                for i in neighbors:
                    node = identificadores[i]
                    if node != jidS and node not in visited:
                        forward.append(node)
                
                for n in forward:
                    visited += ","+n
                
                # En caso de haber nodos a hacer fordward
                if len(forward)>0:
                    print("AQUI")
                    if jumps > 0:
                        payload = ufrom+ "./." +dest+ "./." +str(jumps) + "./." + visited + "./." + the_msg
                        print("AQUI")
                        for i in forward:
                            self.send_message(mto=i,
                                mbody=payload,
                                mtype='chat')
                            print("\n Forwarded to:", i)
                    else:
                        print("\nCantidad de jumps exedida no se hara forward del paquete")
                else:
                    print("\nNo hay a vecinos a quien hacer forward")
            # Si es el nodo destino imprimir estadisticas del paquete
            else:
                print("\nHa recibido un mensaje de: "+ufrom)
                print("El mensaje dice " + the_msg)
                print("Cuenta con "+ str(jumps) + " saltos restantes. Este mensaje a pasado por los nodos: {"+visited+"}")

#Bot que mandad el mensaje, se establece un payload con enviador, destino, saltos, nodos visitados y mensaje
class SendMsgBot(slixmpp.ClientXMPP):

    def __init__(self, jid, password, recipient, message):
        slixmpp.ClientXMPP.__init__(self, jid, password)
        self.recipient = recipient
        self.msg = message
        self.jid = jid
        self.add_event_handler("session_start", self.start)
        self.register_plugin('xep_0030') # Service Discovery
        self.register_plugin('xep_0199') # XMPP Ping

    async def start(self, event):
        self.send_presence()
        await self.get_roster()

        #Determinar mis vecinos
        key = get_key(identificadores, self.jid)
        neighbors = topologia[key]
        
        #Settear el payload
        jumps = len(identificadores) - 1
        nodes_visited = ""
        for n in neighbors:
            nodes_visited += identificadores[n]+","
        nodes_visited = nodes_visited[:-1]

        payload = self.jid+ "./." +self.recipient+ "./." +str(jumps) + "./." + nodes_visited + "./." + self.msg

        #Mandar a todos los vecino si los hay
        if len(neighbors) > 0:
            for neig in neighbors:
                neig_name = identificadores[neig]
                self.send_message(mto=neig_name,
                                mbody=payload,
                                mtype='chat')
        else:
            print("No se tienen vecinos")

        self.disconnect()

#Thread que se encarga de escuchar mensajes y hacer fordward
def listening_thread(jid, upass, loop):
    asyncio.set_event_loop(loop)
    listen = ListenBot(jid, upass)
    listen.connect()
    listen.process()

#Obtener la key de un diccionario mediante un valor
def get_key(dic, val):
    keys = [k for k, v in dic.items() if v == val]
    if keys:
        return keys[0]
    return None

#Main
if __name__ == "__main__":
    #Extraccion de nombres y topologia
    topo = open("topo-demo.txt")
    names = open("names-demo.txt")

    topologia = json.load(topo)['config']
    identificadores = json.load(names)['config']

    #Obtencion de datos del nodo
    jid = input("Ingrese su JID: ")
    upass = getpass.getpass("Ingrese contra: ")

    #Comprobar que el jid sea un nodo valido en la topologia
    if jid in identificadores.values():
        #Iniciar thread de escucha
        loop = asyncio.new_event_loop()
        my_thread = threading.Thread(target=listening_thread, args=(jid, upass,loop), daemon=True)
        my_thread.start()

        #Opcion para mandar mensajes
        while True:
            action = input("Desea mandar un mensaje s/n: ")
            if action == "s":
                dest = input("Ingrese el JID del destino: ")
                msg = input("Ingrese el mensaje: ")
                if dest in identificadores.values():
                    msgBot = SendMsgBot(jid, upass, dest, msg)
                    msgBot.connect()
                    msgBot.process(forever=False)
                    print("Mensaje mandado exitosamente!")
                else:
                    print("Usuario destino no esta dentro de la topologia brindada")
            elif action == "n":
                break
            else:
                print("Esa opcion no existe")
    else:
        print("Ese usuario no se encuentra dentro de la topologia brindada")

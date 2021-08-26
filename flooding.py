import asyncio
from asyncio.events import set_event_loop
import slixmpp
import json
import getpass
import threading
import time

class ListenBot(slixmpp.ClientXMPP):
    def __init__(self, jid, password):
        slixmpp.ClientXMPP.__init__(self, jid, password)
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

    def message(self, msg):
        if msg['type'] in ('chat', 'normal'):
            print("\nRecibio un mensaje de " + str(msg['from']).split('@')[0] + ", el mensaje dice: " + str(msg['body']))

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

        #Send to all neighbors
        key = get_key(identificadores, self.jid)
        neighbors = topologia[key]
        
        #Set message
        jumps = len(identificadores) - 1
        nodes_visited = ""
        for n in neighbors:
            nodes_visited += identificadores[n]+","
        nodes_visited = nodes_visited[:-1]

        payload = str(jumps) + "./." + nodes_visited + "./." + self.msg

        if len(neighbors) > 0:
            for neig in neighbors:
                neig_name = identificadores[neig]
                self.send_message(mto=neig_name,
                                mbody=payload,
                                mtype='chat')
        else:
            print("No se tienen vecinos")

        self.disconnect()

def listening_thread(jid, upass, loop):
    asyncio.set_event_loop(loop)
    listen = ListenBot(jid, upass)
    listen.connect()
    listen.process()

def get_key(dic, val):
    keys = [k for k, v in dic.items() if v == val]
    if keys:
        return keys[0]
    return None

if __name__ == "__main__":
    topo = open("topo-demo.txt")
    names = open("names-demo.txt")

    topologia = json.load(topo)['config']
    identificadores = json.load(names)['config']

    for i in topologia:
        print(i, topologia[i])

    for i in identificadores:
        print(i, identificadores[i])

    jid = input("Ingrese su JID: ")
    upass = getpass.getpass("Ingrese contra: ")

    if jid in identificadores.values():
        loop = asyncio.new_event_loop()
        my_thread = threading.Thread(target=listening_thread, args=(jid, upass,loop), daemon=True)
        my_thread.start()

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

import asyncio
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
        self.add_event_handler("session_start", self.start)
        self.register_plugin('xep_0030') # Service Discovery
        self.register_plugin('xep_0199') # XMPP Ping

    async def start(self, event):
        self.send_presence()
        await self.get_roster()

        self.send_message(mto=self.recipient,
                          mbody=self.msg,
                          mtype='chat')

        self.disconnect()

def listening_thread(jid, upass, loop):
    asyncio.set_event_loop(loop)
    listen = ListenBot(jid, upass)
    listen.connect()
    listen.process()

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

    loop = asyncio.new_event_loop()
    my_thread = threading.Thread(target=listening_thread, args=(jid, upass,loop), daemon=True)
    my_thread.start()

    while True:
        action = input("Desea mandar un mensaje s/n: ")
        if action == "s":
            dest = input("Ingrese el JID del destino: ")
            msg = input("Ingrese el mensaje: ")
            msgBot = SendMsgBot(jid, upass, dest, msg)
            msgBot.connect()
            msgBot.process(forever=False)
        elif action == "n":
            break
        else:
            print("Esa opcion no existe")
import slixmpp
import asyncio
import json

topo = open("topo-demo.txt")
names = open("names-demo.txt")

topologia = json.load(topo)['config']
identificadores = json.load(names)['config']

for i in topologia:
    print(i, topologia[i])

for i in identificadores:
    print(i, identificadores[i])
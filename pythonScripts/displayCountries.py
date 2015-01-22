#!/usr/bin/python
#print "Content-type: text/json\n"
import ConfigParser,json

displayDict = {}
config = ConfigParser.ConfigParser()
config.read('/usr/local/coordinates.cfg')
for i in range(1,6):
    string = "region" + str(i)
    regionName = config.get('regions', string)
    displayDict[regionName] = {}
    displayDict[regionName]['name'] = regionName
    displayDict[regionName]['coords'] = config.get('coordinates', string)
    displayDict[regionName]['status'] = "old"

with open('/tmp/location/regions.alg', 'r') as f:
    data = f.readline().strip()

if(data != ""):
    regionNames = data.split(" ")
else:
    regionNames = []

if(len(regionNames) > 0):
 for i in regionNames:
    regionName = config.get('regions', i)
    if(regionName != ""):
        displayDict[regionName]['name'] = regionName
        displayDict[regionName]['coords'] = config.get('coordinates', i)
        displayDict[regionName]['status'] = "new"

print json.dumps(displayDict)

#!/usr/bin/python
#print "Content-type: text/json\n"
import ConfigParser,json

displayDict = {}
"""
displayDict['Singapore'] = {}
displayDict['Singapore']['name'] = "Singapore"
displayDict['Singapore']['coords'] = [1.3,103.8]
displayDict['Singapore']['status'] = "old"

displayDict['Tokyo'] = {}
displayDict['Tokyo']['name'] = "Tokyo"
displayDict['Tokyo']['coords'] = [35.68,139.69]
displayDict['Tokyo']['status'] = "new"
"""
config = ConfigParser.ConfigParser()
config.read('/usr/local/coordinates.cfg')
for i in range(1,6):
    string = "region" + str(i)
    regionName = config.get('regions', string)
    #regionCoordinates = config.get('coordinates', string)
    displayDict[regionName] = {}
    displayDict[regionName]['name'] = regionName
    displayDict[regionName]['coords'] = config.get('coordinates', string)
    displayDict[regionName]['status'] = "old"

with open('/tmp/regions.alg', 'r') as f:
    data = f.readline().strip()

regionNames = data.split(" ")

for i in regionNames:
    regionName = config.get('regions', i)
    if(regionName != ""):
        displayDict[regionName]['name'] = regionName
        displayDict[regionName]['coords'] = config.get('coordinates', i)
        displayDict[regionName]['status'] = "new"
print json.dumps(displayDict)


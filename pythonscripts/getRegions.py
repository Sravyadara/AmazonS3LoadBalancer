#!/usr/bin/python
#print "Content-type: text/json\n"
from boto.s3.connection import S3Connection
import ConfigParser,json, os

conn = S3Connection()
buckets = conn.get_all_buckets()
config = ConfigParser.ConfigParser()
userHome = os.environ['HOME']
fileName = userHome + "/pythonscripts/regions.cfg"
config.read(fileName)

bucketsDict = {}

for b in buckets:
    bucketsDict[b.name] = config.get('regions', b.get_location())

regionsDict = {}

for key in bucketsDict.keys():
    try:
        regionsDict[bucketsDict[key]]
    except KeyError:
        regionsDict[bucketsDict[key]] = []

    regionsDict[bucketsDict[key]].append(key)

donutDict = {}

for key in regionsDict:
    donutDict[key] = {}
    donutDict[key]['label'] = key
    donutDict[key]['value'] = len(regionsDict[key])

print json.dumps(donutDict)

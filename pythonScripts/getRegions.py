#!/usr/bin/python
#print "Content-type: text/json\n"
from boto.s3.connection import S3Connection
import ConfigParser,json

#conn = S3Connection("AKIAJHA24Y27ZR443WSA","XIEk9GOoXcq9CZvb4Vj1aHSmtAPyGrSMKDc7zZsE")
conn = S3Connection("AKIAIRYQCUTPHAUK6QOA", "BNg14WjSheOhU57OEnBUgVK6RYfjR86ppxBvsViz")
buckets = conn.get_all_buckets()
config = ConfigParser.ConfigParser()
config.read('/var/www/cgi/cloud/regions.cfg')
bucketsDict = {}

for b in buckets:
    bucketsDict[b.name] = config.get('regions', b.get_location())
    #bucketsDict[b.name]['region'] = config.get('regions', b.get_location())

#print bucketsDict
regionsDict = {}

for key in bucketsDict.keys():
    try:
        regionsDict[bucketsDict[key]]
    except KeyError:
        regionsDict[bucketsDict[key]] = []

    regionsDict[bucketsDict[key]].append(key)

#print regionsDict
donutDict = {}

for key in regionsDict:
    donutDict[key] = {}
    donutDict[key]['label'] = key
    donutDict[key]['value'] = len(regionsDict[key])

print json.dumps(donutDict)


#print buckets[0].get_all_keys()[0].size
#print buckets[0].get_all_keys()[0].name


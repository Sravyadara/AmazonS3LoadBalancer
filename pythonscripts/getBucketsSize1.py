#!/usr/bin/python
#print "Content-type: text/json\n"
from boto.s3.connection import S3Connection
from boto.s3.key import Key
import json,os
import ConfigParser

conn = S3Connection()
allBuckets = conn.get_all_buckets()
config = ConfigParser.ConfigParser()
userHome = os.environ['HOME']
fileName = userHome + "/pythonscripts/regions.cfg"
config.read(fileName)

bucketDetails = {}

for bucket in allBuckets:
    allKeys = bucket.get_all_keys()
    size = 0
    bucketDetails[bucket.name] = {}
    for key in allKeys:
        size = size + key.size
    bucketDetails[bucket.name]['size'] = size
    bucketDetails[bucket.name]['region'] = config.get('regions', bucket.get_location())


regionsDict = {}

for key in bucketDetails.keys():
    try:
        regionsDict[bucketDetails[key]['region']]
    except KeyError:
        regionsDict[bucketDetails[key]['region']] = [] 

    regionsDict[bucketDetails[key]['region']].append(key)

barDict = {}

for key in regionsDict:
    barDict[key] = {}
    barDict[key]["category"] = key
    totalSize = 0
    for i in regionsDict[key]:
        totalSize = totalSize + bucketDetails[i]['size']
    barDict[key]["totalSize"] = totalSize / 1000

print json.dumps(barDict)
    



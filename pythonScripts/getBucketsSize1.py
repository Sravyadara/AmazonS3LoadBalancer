#!/usr/bin/python
#print "Content-type: text/json\n"
from boto.s3.connection import S3Connection
from boto.s3.key import Key
import json
import ConfigParser

conn = S3Connection("<aws_access_key>", "<aws_secret_key>")
allBuckets = conn.get_all_buckets()
config = ConfigParser.ConfigParser()
config.read('/usr/local/regions.cfg')

bucketDetails = {}

for bucket in allBuckets:
    allKeys = bucket.get_all_keys()
    size = 0
    bucketDetails[bucket.name] = {}
    #print "%s\t%s" %(bucket.name,bucket.get_location())
    #bucketDetails[bucket.name]["category"] = bucket.name   
    for key in allKeys:
        size = size + key.size
        #bucketDetails[bucket.name]['percentage'] = size /1000
    bucketDetails[bucket.name]['size'] = size
    bucketDetails[bucket.name]['region'] = config.get('regions', bucket.get_location())

#print json.dumps(bucketDetails)

regionsDict = {}

for key in bucketDetails.keys():
    try:
        regionsDict[bucketDetails[key]['region']]
    except KeyError:
        regionsDict[bucketDetails[key]['region']] = [] 

    regionsDict[bucketDetails[key]['region']].append(key)

#print regionsDict
barDict = {}

for key in regionsDict:
    barDict[key] = {}
    barDict[key]["category"] = key
    totalSize = 0
    for i in regionsDict[key]:
        totalSize = totalSize + bucketDetails[i]['size']
    barDict[key]["totalSize"] = totalSize / 1000

print json.dumps(barDict)
    



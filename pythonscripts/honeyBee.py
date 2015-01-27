#!/usr/bin/python
from boto.s3.connection import S3Connection
from boto.s3.key import Key
import sys,os,ast

print "Executing HoneyBee algorithm and uploading photos"
conn = S3Connection()
allBuckets = conn.get_all_buckets()
userHome = os.environ['HOME']
photosLocation = userHome + "/photos/"

bucketsSizes = {}
requestNumber = sys.argv[1]
argument = sys.argv[2]
argument = (argument.rstrip(']')).lstrip('[')
filesList = argument.split(",")

for bucket in allBuckets:
    allKeys = bucket.get_all_keys()
    size = 0
    for key in allKeys:
        size = size + key.size
    bucketsSizes[bucket.name] = size

for i in range(0, int(requestNumber)):
    for value in filesList:
        fileName = photosLocation + value +".bmp"
        bucketToUpload = min(bucketsSizes, key = bucketsSizes.get)
        print "Uploading file: %s to bucket : %s" %(fileName, bucketToUpload)
        k = Key(conn.get_bucket(bucketToUpload))
        k.key = os.path.basename(fileName)
        k.set_contents_from_filename(fileName)
        fileSize = os.path.getsize(fileName)
        print "\nUpdated bucketsSizes\n"
        bucketsSizes[bucketToUpload] += fileSize
        print bucketsSizes

package controllers;

import java.util.ArrayList;
import java.util.List;

import model.BucketDAO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Handler {
	private static AmazonS3 s3;
	
	public  S3Handler(){
			
	        AWSCredentials credentials = null;
	        try {
	            credentials = new ProfileCredentialsProvider("default").getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (/Users/sravyadara/.aws/credentials), and is in valid format.",
	                    e);
	        }
			
	        System.out.println("===========================================");
	        System.out.println("Getting Started with Amazon S3");
	        System.out.println("===========================================\n");
	        
	        s3 = new AmazonS3Client(credentials);
	}
		
		
	public List<BucketDAO> getBucketList(){	
		List<Bucket> bucketList = s3.listBuckets();
		List<BucketDAO> bucketDaoList = new ArrayList<BucketDAO>();
		for(Bucket bucket: bucketList){
			double size = getBucketSize(bucket.getName())/1024.00 ;
			BucketDAO bucketDao = new BucketDAO(bucket.getName(), bucket.getOwner(), bucket.getCreationDate(),
					size);
			List<String> keyList = getKeysByBucket(bucket.getName());
			bucketDao.setKeyList(keyList);
			bucketDaoList.add(bucketDao);
		}
		return bucketDaoList;	
	}
	
	public long getBucketSize(String bucketName){
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        long bucketSize = 0;
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
        	bucketSize += objectSummary.getSize();
        }        
        return bucketSize;
	}
	
	public List<String> getKeysByBucket(String bucketName){
		ObjectListing objectList = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		List<String> keyList = new ArrayList<String>();
		for (S3ObjectSummary objectSummary : objectList.getObjectSummaries()) {
        	keyList.add(objectSummary.getKey());
        } 
	    return keyList;
	}
}

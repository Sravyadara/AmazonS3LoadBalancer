package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.BucketDAO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Handler {
	private static AmazonS3 s3;
    public  String userHome = System.getenv("HOME");
    public  String algorithmPath = userHome + "/location/";
    public  String photosPath = userHome + "/photos/";

	
	public  S3Handler(){
			
	        AWSCredentials credentials = null;
	        try {
	            credentials = new ProfileCredentialsProvider("default").getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (~/.aws/credentials), and is in valid format.",
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
			BucketDAO bucketDao = new BucketDAO(bucket.getName(), bucket.getCreationDate(),
					size);
			List<String> keyList = getKeysByBucket(bucket.getName());
			bucketDao.setKeyList(keyList);
			bucketDao.setCost(getCost(size));
			bucketDaoList.add(bucketDao);
		}
		return bucketDaoList;	
	}
	
	public double getCost(double size){
		double cost = size*0.10; // each MB costs $0.10
		return cost;		
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
	
	public void uploadPhotoToS3(String bucketName, String key) throws IOException{

		Random random = new Random();

        String filePath = photosPath + key + ".bmp";
        
        try {
        	       	
            FileInputStream stream = new FileInputStream(filePath);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key+"_"+random.nextInt(10000)+".bmp", stream, objectMetadata);
            PutObjectResult result = s3.putObject(putObjectRequest);
            //System.out.println("Etag:" + "-->" + result);
            
            //long bucketSize = getBucketSize(bucketName);
            //System.out.println("Bucket Size:" + bucketSize/1024 +" MB");
                        
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        } 

	}
}

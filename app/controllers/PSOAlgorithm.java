package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controllers.S3Handler;
import model.BucketDAO;

public class PSOAlgorithm {
	
	private String bucketNames[] = {"cmpnorthcalidc1","cmpnorthcalidc2","cmporegonidc1","cmporegonidc2","cmporegonidc3","cmpsingaporeidc1","cmpsingaporeidc2","cmpsingaporeidc3","cmpsydneyidc1","cmpsydneyidc2","cmptokyoidc1","cmptokyoidc2"};
    private int SWARM_SIZE = bucketNames.length;       
    private List<BucketDAO> bucketList;
    private S3Handler s3Handler;
    private String bucketToUpload;
    
    public PSOAlgorithm(S3Handler s3Handler){
    	this.s3Handler = s3Handler;
    }
    
   	
	public List<BucketDAO> getBucketList() {
		return bucketList;
	}

	public void setBucketList(List<BucketDAO> bucketList) {
		this.bucketList = bucketList;
	}

	public S3Handler getS3Handler() {
		return s3Handler;
	}

	public void setS3Handler(S3Handler s3Handler) {
		this.s3Handler = s3Handler;
	}
	
	public String getBucketToUpload() {
		execute();
		return bucketToUpload;
	}


	public void setBucketToUpload(String bucketToUpload) {
		this.bucketToUpload = bucketToUpload;
	}

	public void initializeSwarm(){
		bucketList = s3Handler.getBucketList();
//		bucketList = new ArrayList<BucketDAO>();	
//		for(int i=0;i<SWARM_SIZE;i++){
//		
//			BucketDAO bucket = new BucketDAO();
//			bucket.setName(bucketNames[i]);	
//			//bucket.setBucketSize(s3Handler.getBucketSize(bucket.getName()));
//			bucketList.add(bucket);
//		}
//		
//		updateBucketSize();
	}

	
	private void execute(){
		updateBucketSize();
		double leastBucketSize = bucketList.get(0).getBucketSize();
		int bucketNumber = 0;
		
		for(int i=1; i< bucketList.size(); i++){
			if(bucketList.get(i).getBucketSize() < leastBucketSize){
				leastBucketSize = bucketList.get(i).getBucketSize();
				bucketNumber = i;
			}			
		}
		bucketToUpload = bucketList.get(bucketNumber).getName();

	}
	
	private void updateBucketSize(){		
		for(BucketDAO b : bucketList){
			b.setBucketSize(s3Handler.getBucketSize(b.getName()));
		}
	}

}

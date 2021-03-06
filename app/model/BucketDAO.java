package model;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.s3.model.Owner;

public class BucketDAO{
	
	private double bucketSize =0;	
	
    /** The name of this S3 bucket */
    private String name = null;

	/** The details on the owner of this bucket */
    private Owner owner = null;

    /** The date this bucket was created */
    private Date creationDate = null;
    
    /** List contains all the photo names in the bucket */
    private List<String> keyList = null;
    
    /** Charges for the bucket */
    private double cost = 0;
    
    public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	
	public BucketDAO(){
		
	}

	public BucketDAO(String name, Owner owner, Date creationDate, double bucketSize){
    	this.name =  name;
    	this.owner = owner;
    	this.creationDate = creationDate;
    	this.bucketSize = bucketSize;
    }

    public BucketDAO(String name, Date creationDate, double bucketSize){
    	this.name =  name;
    	this.creationDate = creationDate;
    	this.bucketSize = bucketSize;
    }
	
	public double getBucketSize() {
		return bucketSize;
	}
	public void setBucketSize(long bucketSize) {
		this.bucketSize = bucketSize;
	}
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Owner getOwner() {
		return owner;
	}
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
}

package model;

import java.util.Date;

import com.amazonaws.services.s3.model.Owner;

public class BucketDAO{
	
	private double bucketSize =0;	
	
    /** The name of this S3 bucket */
    private String name = null;

	/** The details on the owner of this bucket */
    private Owner owner = null;

    /** The date this bucket was created */
    private Date creationDate = null;
    
    public BucketDAO(String name, Owner owner, Date creationDate, double bucketSize){
    	this.name =  name;
    	this.owner = owner;
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

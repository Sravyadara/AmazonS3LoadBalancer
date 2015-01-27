package controllers;
/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.awt.BorderLayout;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;


/**
 * Demonstrates how to upload data to Amazon S3, and track progress, using a
 * Swing progress bar.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * <p>
 * WANRNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 *
 * http://aws.amazon.com/security-credentials
 */


public class s3locationmodify {

    private static AWSCredentials credentials = null;
    private static TransferManager tx;
    private static String bucketName;
    public static String userHome = System.getenv("HOME");
    public static String algorithmPath = userHome + "/location/";
    public static String photosPath = userHome + "/photos/";

    private JProgressBar pb;
    private JFrame frame;
    private static Upload upload;
    private JButton button;
    private static long availSpaceOregon = 0;
    private static long availSpaceNorthCal = 0;
    private static long availSpaceSingapore = 0;
    private static long availSpaceTokyo = 0;
    private static long availSpaceSydney = 0;
    
    
    private static long maxsize = 1000000000;
   

    public static void main(String[] args) throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (/home/sravya/.aws/credentials).
         *
         * TransferManager manages a pool of threads, so we create a
         * single instance and share it throughout our application.
         */
       try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/home/sravya/.aws/credentials), and is in valid format.",
                    e);
        }
        
        int argLen = args.length;
        Region reg = Region.getRegion(Regions.US_WEST_2);
      
    	 int hack = 0;
    	 int userrequests ;
    	 
    	 try{ 
    		 userrequests = Integer.parseInt(args[argLen-1]);
    	 }
    	 catch (NumberFormatException e)
    	 {
    		 userrequests = 1;
    		 String use =  args[argLen-1];
    		 if(use.equals("Australia"))
    		 {
    			 hack = 0;
    		 }
    		 else if(use.equals("SouthAfrica") )
    		 {
    			 hack = 1;
    		 }
    		 else if(use.equals("India"))
    		 {
    			 hack = 2;
    		 }
    		 else if(use.equals("UnitedKingdom"))
    		 {
    			 hack = 3;
    		 }
    		 else if(use.equals("China"))
    		 {
    			 hack = 4;
    		 }
    		 else if(use.equals("Germany"))
    		 {
    			 hack = 5;
    		 }
    		 else if(use.equals("France"))
    		 {
    			 hack = 6;
    		 }
    		 else if(use.equals("Japan"))
    		 {
    			 hack = 7;
    		 }
    		 else if(use.equals("Thailand"))
    		 {
    			 hack = 8;
    		 }
    		 else if(use.equals("Spain"))
    		 {
    			 hack = 9;
    		 }
    	 }
    	
       int filecount=0;
        for(int m = 0; m < argLen-1;m++)
        {
        	filecount= filecount+1;
        }
        
        int numphotos = filecount;
        int numIDCs= numphotos;
    	
        int locationInd= 0;
        long[] cusize= new long[6];
        long photosspace=0;
        
        for(int mm = 0; mm < userrequests;mm++)
        {
        for(int i=0;i<cusize.length;i++)
        	cusize[i] = 0;
       
        ArrayList<Integer> originalgarph = new ArrayList<Integer>();
       
        
        loadObj ob = calculateload();
        long[] regionload = new long[5];
        regionload = ob.load;
        for(int i=0;i<regionload.length;i++)
        {  if(regionload[i]==0)
        {
        	regionload[i] = 1000;
        }
        }
        /*for (int i=0; i<5 ;i++)
        { System.out.println(regionload[i]);
          double diffload=0; 
          double avgload=0;
          int count=0;
         
           for(int j=0;j<5; j++)
           {
        	   if(j!=i)
        	   {
        		   avgload = avgload + regionload[j];
        		   count++;
        		   
        	   }
        	   	   
        	   
           }
           
          
           avgload= (avgload / count);
           diffload= (regionload[i]/avgload) ;
           System.out.println("avgload: "+ avgload);
           System.out.println("diffload: "+ diffload);
           if(diffload < 1.8)
           {
        	   
        	   originalgarph.add(i+1);
          }
        	
        }*/
             for(int i=0;i<5;i++)
             {
            	 originalgarph.add(i+1);
            	 
             }
        availSpaceNorthCal = maxsize - regionload[0];
        photosspace = numphotos*6000;
		 if(availSpaceNorthCal < photosspace)
		 {
			availSpaceNorthCal = maxsize;
			cusize[1]= availSpaceNorthCal/6000 ;
			
		 }
		 else
		 {
			 cusize[1]=availSpaceNorthCal/6000;
		 }
		 availSpaceOregon = maxsize - regionload[1];
	        photosspace = numphotos*6000;
			 if(availSpaceOregon < photosspace)
			 {
				 availSpaceOregon = maxsize;
				cusize[2]= availSpaceOregon/6000 ;
				
			 }
			 else
			 {
				 cusize[2]=availSpaceOregon/6000;
			 }
			 availSpaceSingapore = maxsize - regionload[2];
		        photosspace = numphotos*6000;
				 if(availSpaceSingapore < photosspace)
				 {
					 availSpaceSingapore = maxsize;
					cusize[3]= availSpaceSingapore/6000 ;
					
				 }
				 else
				 {
					 cusize[3]=availSpaceSingapore/6000;
				 }
				 availSpaceTokyo = maxsize - regionload[3];
			        photosspace = numphotos*6000;
					 if(availSpaceTokyo < photosspace)
					 {
						 availSpaceTokyo = maxsize;
						cusize[4]= availSpaceTokyo/6000 ;
						
					 }
					 else
					 {
						 cusize[4]=availSpaceTokyo/6000;
					 }
					 availSpaceSydney = maxsize - regionload[4];
				        photosspace = numphotos*6000;
						 if(availSpaceSydney < photosspace)
						 {
							 availSpaceSydney = maxsize;
							cusize[5]= availSpaceSydney/6000 ;
							
						 }
						 else
						 {
							 cusize[5]=availSpaceSydney/6000;
						 }
		 
     
		 int cou = originalgarph.size();
            String fileName = algorithmPath + "request.alg";
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");

    	
    	for (int i = 0; i < cou; i++) {
    	    int value = originalgarph.get(i);
    	    writer.print(value+" ");
    	    System.out.println(" IDC " + String.valueOf(value) );
    	}
        writer.println();
        for (int i = 0; i < originalgarph.size(); i++) {
        	int j= originalgarph.get(i);
    	  
    	    writer.print(cusize[j]+" ");
    	    
    	}
        writer.println();
        writer.println(1);
        if(userrequests != 1)
        {
        	locationInd = randInt(0,9);
        	
        }
        else
        {
        	locationInd = hack;
        }
        	 writer.println(locationInd);
        	 System.out.println(" locationInd " + String.valueOf(locationInd) );
             writer.println(numphotos);	
             System.out.println(" numphotos " + String.valueOf(numphotos) );
         	 writer.println(numIDCs);
         	 System.out.println(" numIDCs " + String.valueOf(numIDCs) );
             writer.close();
             originalgarph.clear();
          
        try{
            
            String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
            String pythonCmd = "/usr/bin/python " + algorithmPath + "ramd.py";
            Process p = Runtime.getRuntime().exec(pythonCmd);
           
            try {
                Thread.sleep(2000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            String fileName1 = algorithmPath + "work.alg";
            File log = new File(fileName1);             
            
           
            int filenumber=0;

             String fileName2 = algorithmPath + "filenumber.alg";
            Scanner numberscan = new Scanner(new File(fileName2));
            
            if(numberscan.hasNextLine())
            {
            	filenumber = numberscan.nextInt();
            	
            }
            else
            {
            	filenumber = 1;
            	
            }
            numberscan.close();
          
            ArrayList<String> photofnames = new ArrayList<String>();
            ArrayList<String> argFNames = new ArrayList<String>();
            for(int ll = 0; ll < argLen-1;ll++)
            {
            	
                photofnames.add(photosPath + args[ll] + ".bmp");
            	argFNames.add(args[ll]);
            	System.out.println("Will upload " + photosPath + args[ll] + ".bmp");
            }

            String sCurrentLine;
            BufferedReader br = null;
			br = new BufferedReader(new FileReader(fileName1));
			int currLine = 0;
			Integer userNumber=0;
			ArrayList<Integer> idcSet = new ArrayList<Integer>();
			ArrayList<Integer> photonos = new ArrayList<Integer>();
			int currU = 0;
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(currLine %3 == 0)
				{
					userNumber = Integer.parseInt(sCurrentLine);
					idcSet.clear();
					photonos.clear();
				}
				if(currLine %3 == 1)
				{
					String[] idcnums = sCurrentLine.split(" ");
					String regions="";
                                        String regionsFileName = algorithmPath + "regions.alg";
					PrintWriter regionwriter = new PrintWriter(regionsFileName, "UTF-8");    
					
					for(int numIdcs = 0; numIdcs < idcnums.length;numIdcs++)
					{
						idcSet.add(Integer.parseInt( idcnums[numIdcs]));
						
				    	
						
						if(idcnums[numIdcs].equals("1"))
						{  
							regions="region1";
							regionwriter.print(regions+" ");	
						}
						else if(idcnums[numIdcs].equals("2"))
						{  
							regions="region2";
							regionwriter.print(regions+" ");
							
						}
						else if(idcnums[numIdcs].equals("3"))
						{  
							regions="region3";
						    regionwriter.print(regions+" ");
							
						}
						else if(idcnums[numIdcs].equals("4"))
						{  
							regions="region4";
					        regionwriter.print(regions+" ");
							
						}
						else if(idcnums[numIdcs].equals("5"))
						{
							
							regions="region5";
						    regionwriter.print(regions+" ");
						}
						
						System.out.println("IDCs: "+ idcnums[numIdcs]);
					}
					regionwriter.close();
				}
				if(currLine %3 == 2)
				{
					String[] idcpnums = sCurrentLine.split(" ");
					for(int numIdcs = 0; numIdcs < idcpnums.length;numIdcs++)
					{
						photonos.add(Integer.parseInt( idcpnums[numIdcs]));
						
					}
					ArrayList<String> smallestBnames = new ArrayList<String>();
					ArrayList<String> bucketnames = new ArrayList<String>();
					for(int tot = 0; tot < idcSet.size();tot++)
					{
						smallestBnames.add(ob.bnames[idcSet.get(tot)-1]);
					}
					AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
					int currPno = 0;
					ArrayList<String> transferThese = new ArrayList<String>();
					ArrayList<Integer> transferThesebno = new ArrayList<Integer>();
					System.out.println(String.valueOf(idcpnums.length));
					//upload everything to 1 bucket
					for(int numIdcs = 0; numIdcs < idcpnums.length;numIdcs++)
					{ 
						for(int numP=0; numP < photonos.get(numIdcs); numP++)
						{
							String uploadFileName = photofnames.get(currPno);
							String keyName        = String.valueOf(userNumber) + "_" + argFNames.get(currPno) + '_' +filenumber++ +".bmp";
		                	if(numIdcs > 0)
		                	{
		                		transferThese.add(keyName);
		                		transferThesebno.add(numIdcs);
		                		
		                	}
		                	try {
	                            System.out.println("Uploading "+ uploadFileName +" to " + smallestBnames.get(0)+ " with keyname "+ keyName);
	                            File file = new File(uploadFileName);
	                            s3client.putObject(new PutObjectRequest(
	                            		smallestBnames.get(0), keyName, file));
	                            
	                     
	                         } catch (AmazonServiceException ase) {
	                            System.out.println("Caught an AmazonServiceException, which " +
	                            		"means your request made it " +
	                                    "to Amazon S3, but was rejected with an error response" +
	                                    " for some reason.");
	                            System.out.println("Error Message:    " + ase.getMessage());
	                            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	                            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	                            System.out.println("Error Type:       " + ase.getErrorType());
	                            System.out.println("Request ID:       " + ase.getRequestId());
	                        } 
							currPno++;
						}
					}
					//transfer other files
					System.out.println("Number of files to transfer " + String.valueOf(transferThese.size()));
					for(int tot = 0; tot < transferThese.size(); tot++)
					{
						String source = smallestBnames.get(0);
						String dest = smallestBnames.get(transferThesebno.get(tot));
						String fname = transferThese.get(tot);
						String src = "s3://"+source+"/"+fname;
						String d = "s3://"+dest;
						String cmd = "aws s3 mv "+ src + " " + d + "\n";
						System.out.println("Moving "+src +" to "+ d +"\n");
						Process p1 = Runtime.getRuntime().exec(cmd);
						
					}
					currU++;
					if(currU >= 1)
					{
						transferThese.clear();
						transferThesebno.clear();
						photofnames.clear();
						argFNames.clear();
						smallestBnames.clear();
						break;
					}
				}
				currLine++;
				
				
			}
                         String fileNumberFilePath = algorithmPath + "filenumber.alg";
			 PrintWriter numberwriter = new PrintWriter(fileNumberFilePath, "UTF-8");
			 
          numberwriter.println(filenumber);
          numberwriter.close();
            
           }catch(Exception e){}
        }
//        String s = null;
//        Process p1 = Runtime.getRuntime().exec("ls -alrt");
//        
//        BufferedReader stdInput = new BufferedReader(new
//             InputStreamReader(p1.getInputStream()));
//
//        BufferedReader stdError = new BufferedReader(new
//             InputStreamReader(p1.getErrorStream()));
//
//        // read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        while ((s = stdInput.readLine()) != null) {
//            System.out.println(s);
//        }
    }

        //new S3TransferProgressSample();
   

    public s3locationmodify() throws Exception {
        /*frame = new JFrame("Amazon S3 File Upload");
        button = new JButton("Choose File...");
        button.addActionListener(new ButtonListener());

        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);

        frame.setContentPane(createContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            int showOpenDialog = fileChooser.showOpenDialog(frame);
            if (showOpenDialog != JFileChooser.APPROVE_OPTION) return;

            //createAmazonS3Bucket();

            ProgressListener progressListener = new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    if (upload == null) return;

                    pb.setValue((int)upload.getProgress().getPercentTransferred());

                    switch (progressEvent.getEventCode()) {
                    case ProgressEvent.COMPLETED_EVENT_CODE:
                        pb.setValue(100);
                        break;
                    case ProgressEvent.FAILED_EVENT_CODE:
                        try {
                            AmazonClientException e = upload.waitForException();
                            JOptionPane.showMessageDialog(frame,
                                    "Unable to upload file to Amazon S3: " + e.getMessage(),
                                    "Error Uploading File", JOptionPane.ERROR_MESSAGE);
                        } catch (InterruptedException e) {}
                        break;
                    }
                }
            };

            File fileToUpload = fileChooser.getSelectedFile();
            PutObjectRequest request = new PutObjectRequest(
                    bucketName, fileToUpload.getName(), fileToUpload)
                .withGeneralProgressListener(progressListener);
            upload = tx.upload(request);
        }
    }

    private void createAmazonS3Bucket() {
        try {
            if (tx.getAmazonS3Client().doesBucketExist(bucketName) == false) {
                tx.getAmazonS3Client().createBucket(bucketName);
            }
        } catch (AmazonClientException ace) {
            JOptionPane.showMessageDialog(frame, "Unable to create a new Amazon S3 bucket: " + ace.getMessage(),
                    "Error Creating Bucket", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    
    private static long calcLoadOneBucket(String bucketName) throws Exception{
    	Process pq = Runtime.getRuntime().exec("aws s3 ls s3://" + bucketName);
        
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(pq.getInputStream()));
       String sq;
       long total = 0;
        while ((sq = stdInput.readLine()) != null) {
           
            String[] thisLine = sq.split("  {0,}");
           
            total = total + Integer.parseInt(thisLine[2]);
        }
        return(total);
    }

    private static loadObj calculateload() throws Exception{
    	long[] load = new long[5];
    	String[] lowbuckets = new String[5];
    	ArrayList<String> bucketnames = new ArrayList<String>();
    	
    	for(int tot = 0; tot < 5;tot++)
		{
    		String bu = null;
    		long min_val = Long.MAX_VALUE;
			switch(tot)
			{
			case 0:
				bucketnames.add("northcalidc1");
				bucketnames.add("northcalidc2");
				break;
			case 1:
				bucketnames.add("oregonidc1");
				bucketnames.add("oregonidc2");
				bucketnames.add("oregonidc3");
				break;
			case 2:
				bucketnames.add("singaporeidc1");
				bucketnames.add("singaporeidc2");
				bucketnames.add("singaporeidc3");
				break;
			case 3:
				bucketnames.add("sydneyidc1");
				bucketnames.add("sydneyidc2");
				break;
			case 4:
				bucketnames.add("tokyoidc1");
				bucketnames.add("tokyoidc2");
				break;
			}
			long totLoad = 0;
			for(int i = 0; i < bucketnames.size();i ++)
			{
				System.out.println("Calculating load for bucket "+ bucketnames.get(i));
				long thisLoad = calcLoadOneBucket(bucketnames.get(i));
				totLoad = totLoad + thisLoad;
				if( min_val > thisLoad)
				{
					min_val = thisLoad;
					bu = bucketnames.get(i);
				}
			}
			load[tot] = totLoad;	
			lowbuckets[tot] = bu;
			bucketnames.clear();
		}
 
       loadObj ob  = new loadObj(); 
        ob.load = load;
        ob.bnames = lowbuckets;
    	
    	return ob;
    }
  
  
private static String calcMin(ArrayList<String> bucketnames )
{
	long min = Long.MAX_VALUE;
	String idc = null;long currVal;
	for(int c = 0 ; c < bucketnames.size(); c++)
	{
		currVal = calculateregionload(bucketnames.get(c));
		if (currVal < min)
		{
			idc = bucketnames.get(c);
			min = currVal;
		}
		
	}
	return(idc);
}
private static long calculateregionload(String regionname){
    	
    	AmazonS3 s3 = new AmazonS3Client(credentials);
    	 long size= 0;
        	 
                 
                     AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
                     //try {
                      ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                             .withBucketName(regionname);
                             
                         ObjectListing objectListing;            
                         do {
                             objectListing = s3client.listObjects(listObjectsRequest);
                             
                          		 for (S3ObjectSummary objectSummary : 
                                      	objectListing.getObjectSummaries()) {
                             			size= objectSummary.getSize();
                            	 }
                            listObjectsRequest.setMarker(objectListing.getNextMarker());
                             
                             
                         } while (objectListing.isTruncated());
                         
                  
         
    	
    	return size;
    }
   

    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(pb);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(panel, BorderLayout.NORTH);
        borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return borderPanel;
    }
}
package controllers;

import java.util.List;
import java.util.Set;

import model.BucketDAO;

import com.amazonaws.services.s3.model.Bucket;
import com.google.gson.Gson;

import play.*;
import play.mvc.*;
import views.html.*;
  
public class Application extends Controller {
	
	 private static  S3Handler s3handler = new S3Handler();
	   

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    
    public static Result listBuckets(){
      response().setHeader("Content-Type", "application/json");
      response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
      response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
      response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
      response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
  	  List<BucketDAO> Buckets = s3handler.getBucketList() ;
  	  Gson gson = new Gson();
  	  String jsonResponse = gson.toJson(Buckets);
  	  return ok(jsonResponse);
    }
    
    
    public static Result bucketSize(String bucketName){
        response().setHeader("Content-Type", "application/json");
        response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
        response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
        response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");         // Ensure this header is also allowed!  
  	  long BucketSize = s3handler.getBucketSize(bucketName) ;
  	  Gson gson = new Gson();
  	  String jsonResponse = gson.toJson(BucketSize);
  	  return ok(jsonResponse);
    }
    
}

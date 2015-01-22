package controllers;

import java.util.List;
import java.util.Map;
import model.BucketDAO;
import com.google.gson.Gson;
import play.mvc.*;
import views.html.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

  
public class Application extends Controller {
	
	 private static  S3Handler s3handler = new S3Handler();
     public  static String userHome = System.getenv("HOME");	   

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result bucketSize(String bucketName){
      printHeaders();
  	  long BucketSize = s3handler.getBucketSize(bucketName) ;
  	  Gson gson = new Gson();
  	  String jsonResponse = gson.toJson(BucketSize);
  	  return ok(jsonResponse);
    }
        
    public static Result getBucketDetails(){
    	printHeaders();
    	List<BucketDAO> buckets = s3handler.getBucketList() ;
    	Gson gson = new Gson();
    	String jsonResponse = gson.toJson(buckets);
    	return ok(jsonResponse);
    }
    
    public static Result Requests(String reqNum) throws Exception{
           printHeaders();
       String jsonResponse;
           //System.out.println("sample----------------    test "+ reqNum);
           //System.out.println(request().body().toString());
           Map<String,String[]> jsonMap = request().body().asFormUrlEncoded();
           //TODO check for arrayOutOfBounds Exception if userName is not present in incoming request
           String json = jsonMap.get("username")[0];
           String newJson = json.substring(2, json.length()-2);
           String st[] = newJson.split(",");
           String newSt[] = new String[st.length];
           String myargs[] = new String[st.length+1];
           String justnums[] = new String[2];
           for(int i=0; i< st.length; i++)
           {
               newSt[i] = st[i].replaceAll("\"", "");
               //System.out.println(newSt[i]);
               justnums = newSt[i].split(" ");
               myargs[i] = justnums[1];
               //System.out.println(myargs[i]);
           }
           myargs[st.length] = reqNum;
           s3modify alg = new s3modify();


           alg.main(myargs);
           System.out.println("Done with alg");
                  return ok("success");
   }

   public static Result getRegions() throws IOException{
       printHeaders(); 
	   String jsonResponse = "";
       String cmd = "/usr/bin/python " + userHome + "/pythonscripts/getRegions.py";
       Process p = Runtime.getRuntime().exec(cmd);
       BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
       Object line;
       while((line = in.readLine()) != null) {
            jsonResponse = jsonResponse + line;
       }
                             
       return ok(jsonResponse);
    } 
   
   public static Result getRegionsSize() throws IOException{
	   printHeaders(); 
	   String jsonResponse = "";
       String cmd = "/usr/bin/python " + userHome + "/pythonscripts/getBucketsSize1.py";
       Process p = Runtime.getRuntime().exec(cmd);
       BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
       Object line;
       while((line = in.readLine()) != null) {
            jsonResponse = jsonResponse + line;
       }
	   return ok(jsonResponse);
   }
   
   public static void printHeaders(){
       response().setHeader("Content-Type", "application/json");
       response().setHeader("Access-Control-Allow-Origin", "*");       // Need to add the correct domain in here!!
       response().setHeader("Access-Control-Allow-Methods", "GET");   // Only allow POST
       response().setHeader("Access-Control-Max-Age", "300");          // Cache response for 5 minutes
       response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");  	   	
   }

   public static Result displayCountries() throws IOException {
       printHeaders();
       String jsonResponse = "";
       String cmd = "/usr/bin/python " + userHome + "/pythonscripts/displayCountries.py";
       Process p = Runtime.getRuntime().exec(cmd);
       BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
       Object line;
       while((line = in.readLine()) != null) {
           jsonResponse = jsonResponse + line;
       }
       return ok(jsonResponse);

   }
   
   
}

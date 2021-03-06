package ws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;




import org.apache.http.entity.mime.MultipartEntity;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;



import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

 @SuppressWarnings("deprecation")
public class WebServiceTask  {
	 
    public static final int POST_TASK = 1;
    public static final int GET_TASK = 2; 
    public static final int PUT_TASK = 3;
    private static final String TAG = "WebServiceTask";
    // connection timeout, in milliseconds (waiting to connect)
    private static final int CONN_TIMEOUT = 3000;
    // socket timeout, in milliseconds (waiting for data)
    private static final int SOCKET_TIMEOUT = 5000;
    private int taskType = GET_TASK;
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ProgressDialog pDlg = null;
    
    private String Domain="acr2.programame.com";
    private String path = "/";
    
    private HttpContext localContext=null;
    private CookieStore cookieStore;
    private String token="";
    
    private JSONObject json=null;
    
    
    
    public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	private String Response;
    private URI uriInfo;
    private String FileName;

    public WebServiceTask(int taskType, String processMessage,String token) {
        this.taskType = taskType;
        this.token=token;
    }
    

    public void addNameValuePair(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }
    
    public void Execute(String... urls){
    	this.Response=this.doInBackground(urls);
    }


    protected String doInBackground(String... urls) {
        String url = urls[0];
        String result = "";
        HttpResponse response = doResponse(url);
        if (response == null) {
            return result;
        } else {
            try {
                if (response.getEntity() != null) result = inputStreamToString(response.getEntity().getContent());
                if (taskType == POST_TASK) result = Integer.toString(response.getStatusLine().getStatusCode());
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);

            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

        }
        
        
        return result;
    }
     
    // Establish connection and socket (data retrieval) timeouts
    private HttpParams getHttpParams() {   
        HttpParams htpp = new BasicHttpParams();   
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);   
        return htpp;
    }
     
    private HttpResponse doResponse(String url) {         
        // Use our connection and data timeouts as parameters for our
        // DefaultHttpClient
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        int responseCode=0;
       
        // Create a local instance of cookie store
        //CookieStore cookieStore = new BasicCookieStore();     
        // Create local HTTP context
        //HttpContext localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        //localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore); 
        //CookieManager cookieManager= CookieManager.getInstance();
        this.localContext=this.getLocalContext();
        BasicClientCookie cookie = new BasicClientCookie("acrsession", this.token);
        cookie.setDomain(this.Domain);
        cookie.setPath(this.path);
        this.cookieStore.addCookie(cookie);
        localContext.setAttribute(ClientContext.COOKIE_STORE, this.cookieStore); 
        
        HttpResponse response = null; 
        try {
            switch (taskType) {

            case POST_TASK:
                HttpPost httppost = new HttpPost(url);
                //httppost.addHeader("accept", "application/json");
                if (this.FileName != null){
                	httppost.addHeader("Content-Type", "multipart\form-data");
                	MultipartEntity mp = new MultipartEntity();
                	File file = new File(this.FileName);
                	ContentBody cb = new FileBody(file,"image/jpeg");
                	mp.addPart("userfile",cb);
                	httppost.setEntity(mp);
                	response = httpclient.execute(httppost,localContext);
    				responseCode = response.getStatusLine().getStatusCode();
                }
                else{
                	httppost.addHeader("Content-Type", "application/json");
                	 // Add parameters           
    	            StringEntity se = new StringEntity(json.toString());
    	            httppost.setEntity(se);
                    //httppost.setEntity(new UrlEncodedFormEntity(params));
                    int executeCount = 0;
        			do
        			{
        				//pDlg.setMessage("Logging in.. ("+(executeCount+1)+"/5)");
        				// Execute HTTP Post Request
        				executeCount++;
        				response = httpclient.execute(httppost,localContext);
        				responseCode = response.getStatusLine().getStatusCode();   			
        				// If you want to see the response code, you can Log it
        				// out here by calling:
        				// Log.d("256 Design", "statusCode: " + responseCode)
        			} while (executeCount < 5 && responseCode == 408); 
                }
                        
                uriInfo = httppost.getURI();               
                break;
            case GET_TASK:
                HttpGet httpget = new HttpGet(url);
                httpget.addHeader("accept", "application/json");
                response = httpclient.execute(httpget,localContext);
                responseCode = response.getStatusLine().getStatusCode();
                httpget.getRequestLine();
                uriInfo = httpget.getURI();                    
                break;
            case PUT_TASK:
                HttpPut httpput = new HttpPut(url);
                httpput.addHeader("accept", "application/json");
                httpput.addHeader("Content-Type", "application/json");
                if(this.FileName!=null){
	                File file = new File(this.FileName);
	                InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
	                reqEntity.setContentType("binary/octet-stream");
	                reqEntity.setChunked(true); // Send in multiple parts if needed
	                httpput.setEntity(reqEntity);
                }
                if(this.json!=null){
                	httpput.setEntity(new ByteArrayEntity(this.json.toString().getBytes()));
                }
                response = httpclient.execute(httpput,localContext);
                responseCode = response.getStatusLine().getStatusCode();
                httpput.getRequestLine();
                uriInfo = httpput.getURI();                    
                break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return response;
    }
     
    private String inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        // Return full string
        this.Response=total.toString();
        return total.toString();
    }
    public String getResponse(){
    	return this.Response;
    }
    public HttpContext getLocalContext()
    {
        
		if (localContext == null)
        {
            localContext = new BasicHttpContext();
            cookieStore = new BasicCookieStore();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);// to make sure that cookies provided by the server can be reused
        }
        return localContext;
    }
}
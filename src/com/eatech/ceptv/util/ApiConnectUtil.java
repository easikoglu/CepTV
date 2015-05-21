package com.eatech.ceptv.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.eatech.ceptv.application.MyApplication;
import com.eatech.ceptv.bean.ApiUserRequest;
import com.eatech.ceptv.bean.ResourceResponse;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author erhanasikoglu
 */
public class ApiConnectUtil {
   private static final String TAG = "ApiConnectUtil";


   public static String API_URL = "http://104.236.53.43:8080/api/";


   public static void auth(String applicationId,String clientId,Context mContext){


      WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, mContext);



      ApiUserRequest request = new ApiUserRequest(applicationId,clientId);

      wst.addObject(request);
      wst.setMethodType(MethodType.AUTH);

      // the passed String is the URL we will POST to
      String serviceUrl = API_URL + "auth/login";
      wst.execute(new String[] { serviceUrl });
   }

   public static void getChannels(Context mContext,String sessionId) {


      if(sessionId!=null) {
         WebServiceTask wst = new WebServiceTask(WebServiceTask.GET_TASK, mContext);

         wst.setMethodType(MethodType.CHANNEL_MAIN_LIST);
         // the passed String is the URL we will POST to
         String serviceUrl = API_URL + "channel/findWithCurrentProgram?t=" + sessionId;
         wst.execute(new String[]{serviceUrl});
      }else {
       }

    }


   private static class WebServiceTask extends AsyncTask<String, Integer, String> {

      public static final int POST_TASK = 1;
      public static final int GET_TASK = 2;

      private static final String TAG = "WebServiceTask";

      // connection timeout, in milliseconds (waiting to connect)
      private static final int CONN_TIMEOUT = 3000;

      // socket timeout, in milliseconds (waiting for data)
      private static final int SOCKET_TIMEOUT = 5000;

      private int taskType = GET_TASK;

      private Context mContext;

      private MethodType methodType;

      public WebServiceTask(int taskType, Context mContext) {
         this.mContext = mContext;
         this.taskType = taskType;
       }

      private Object obj;


      public void addObject(Object obj){
         this.obj = obj;
      }

      public void setMethodType(MethodType methodType){
         this.methodType = methodType;
      }



      @Override
      protected void onPreExecute() {


      }

      @Override
      protected String doInBackground(String... urls) {

         String url = urls[0];
         String result = "";

         HttpResponse response = doResponse(url);

         if (response == null) {
            return result;
         } else {

            try {

               result = inputStreamToString(response.getEntity().getContent());

            } catch (IllegalStateException e) {
               Log.e(TAG, e.getLocalizedMessage(), e);

            } catch (IOException e) {
               Log.e(TAG, e.getLocalizedMessage(), e);
            }

         }

         return result;
      }

      @Override
      protected void onPostExecute(String response) {

            handleResponse(response,mContext,methodType);
      }

      // Establish connection and socket (data retrieval) timeouts
      private HttpParams getHttpParams() {

         HttpParams http = new BasicHttpParams();

         HttpConnectionParams.setConnectionTimeout(http, CONN_TIMEOUT);
         HttpConnectionParams.setSoTimeout(http, SOCKET_TIMEOUT);


         return http;
      }

      private HttpResponse doResponse(String url) {

         // Use our connection and data timeouts as parameters for our
         // DefaultHttpClient
         HttpClient httpclient = new DefaultHttpClient(getHttpParams());

         HttpResponse response = null;

         try {
            switch (taskType) {

               case POST_TASK:
                  HttpPost httppost = new HttpPost(url);

                  Gson gson = new Gson();

                  // convert java object to JSON format,
                  // and returned as JSON formatted string
                  String json = gson.toJson(obj);

                  httppost.setEntity(new StringEntity(json));
                  // Add parameters
                  httppost.setHeader("Content-Type",
                     "application/json;charset=UTF-8");

                  response = httpclient.execute(httppost);
                  break;
               case GET_TASK:
                  HttpGet httpget = new HttpGet(url);
                  response = httpclient.execute(httpget);
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
         return total.toString();
      }

   }


   public static void handleResponse(String response,Context mContext,MethodType methodType) {

      try {
         switch (methodType){
            case AUTH:
               ((MyApplication)mContext).setSessionId(response);
               break;
            case CHANNEL_MAIN_LIST:
               ResourceResponse object = getObject(response, ResourceResponse.class);

               //((MainActivity)mContext).handleChannelListAsyncTask((List<ChannelResponse>) object.getResponse());
                break;
         }


      } catch (Exception e) {
         Log.e(TAG, e.getLocalizedMessage(), e);
      }

   }


   public static <T> T getObject(final String jsonString, final Class<T> objectClass) {
      Gson gson = new Gson();
      return gson.fromJson(jsonString, objectClass);
   }

   private enum MethodType{
      AUTH,CHANNEL_MAIN_LIST,PROGRAM
   }

}

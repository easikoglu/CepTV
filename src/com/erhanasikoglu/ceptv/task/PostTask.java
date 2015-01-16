package com.erhanasikoglu.ceptv.task;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
 * An AsyncTask implementation for performing POSTs on the Hypothetical REST APIs.
 */
public class PostTask extends AsyncTask<String, String, String> {
   private static final String TAG = "PostTask";
   private String mRestUrl;
   private RestTaskCallback mCallback;
   private Object mRequestBody;
   // connection timeout, in milliseconds (waiting to connect)
   private static final int CONN_TIMEOUT = 3000;

   // socket timeout, in milliseconds (waiting for data)
   private static final int SOCKET_TIMEOUT = 5000;

   /**
    * Creates a new instance of PostTask with the specified URL, callback, and
    * request body.
    *
    * @param restUrl     The URL for the REST API.
    * @param callback    The callback to be invoked when the HTTP request
    *                    completes.
    * @param requestBody The body of the POST request.
    */
   public PostTask(String restUrl, Object requestBody, RestTaskCallback callback) {
      this.mRestUrl = restUrl;
      this.mRequestBody = requestBody;
      this.mCallback = callback;
   }

   @Override
   protected String doInBackground(String... arg0) {
      String result = "";

      HttpResponse response = doResponse(mRestUrl);

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
   protected void onPostExecute(String result) {
      mCallback.onTaskComplete(result);
      super.onPostExecute(result);
   }


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

         HttpPost httppost = new HttpPost(url);

         Gson gson = new Gson();

         // convert java object to JSON format,
         // and returned as JSON formatted string
         String json = gson.toJson(mRequestBody);

         httppost.setEntity(new StringEntity(json));
         // Add parameters
         httppost.setHeader("Content-Type",
            "application/json;charset=UTF-8");

         response = httpclient.execute(httppost);

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

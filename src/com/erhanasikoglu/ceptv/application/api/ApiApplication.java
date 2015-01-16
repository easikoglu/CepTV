package com.erhanasikoglu.ceptv.application.api;

import android.content.Context;
import android.content.SharedPreferences;
import com.erhanasikoglu.ceptv.bean.ApiUserRequest;
import com.erhanasikoglu.ceptv.task.GetTask;
import com.erhanasikoglu.ceptv.task.PostTask;
import com.erhanasikoglu.ceptv.task.RestTaskCallback;
import com.erhanasikoglu.ceptv.util.LiveTvConstants;
import com.erhanasikoglu.ceptv.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author erhanasikoglu
 */
public class ApiApplication implements ApiInterface {

   public static ApiApplication instance = null;

   public Context context;


   public static ApiApplication getInstance() {

      if (ObjectUtil.isNull(instance)) {
           instance = new ApiApplication();
      }
      return instance;
   }


   public void authResource(String sessionId, final PostCallback callback) {

      checkAuthentication(sessionId, this, MethodType.AUTH, null, callback,null);

   }


   public void channelResource(String sessionId, GetResponseCallback callback) {


      checkAuthentication(sessionId, this, MethodType.CHANNEL_LIST, callback, null,null);
   }


   public void programsResource(String sessionId,String channelId,GetResponseCallback callback){
      Map paramterMap = new HashMap();
      paramterMap.put("channelId",channelId);
      checkAuthentication(sessionId, this, MethodType.PROGRAM_LIST, callback, null, paramterMap);

   }

   /**
    * Request a User Profile from the REST server.
    *
    * @param applicationId
    * @param clientId
    * @param callback      Callback to execute when the profile is available.
    */
   public void auth(String applicationId, String clientId, final PostCallback callback) {
      String restUrl = LiveTvConstants.API_URL;
      restUrl += "auth/login";

      ApiUserRequest request = new ApiUserRequest(applicationId, clientId);

      new PostTask(restUrl, request, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {
            callback.onSessionIdReceived(result);
         }
      }).execute();
   }


   @Override
   public void getChannels(String sessionId, final GetResponseCallback callback) {

      String restUrl = LiveTvConstants.API_URL;
      restUrl += "channel/findAll?t=" + sessionId;

      new GetTask(restUrl, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {

            callback.onChannelListReceived(result);
         }
      }).execute();

   }

   @Override
   public void getPrograms(String sessionId, String channelId, final GetResponseCallback callback) {
      String restUrl = LiveTvConstants.API_URL;
      restUrl += "program/findByChannel/"+channelId+"?t=" + sessionId;

      new GetTask(restUrl, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {

            callback.onProgramListReceived(result);
         }
      }).execute();
   }

   public void checkAuthentication(final String sessionId,
                                   final ApiInterface apiInterface,
                                   final MethodType type,
                                   final GetResponseCallback getCallback,
                                   final PostCallback postCallback,
                                   final Map parameterMap) {


      this.checkTokenValid(sessionId, new GetResponseCallback() {

         @Override
         public void isValidToken(String result) {

            if (!"".equals(result) && result != null) {

               boolean valid = Boolean.parseBoolean(result);
               if (valid) {
                  switch (type) {

                     case AUTH:
                        apiInterface.auth(LiveTvConstants.APPLICATION_API_KEY, LiveTvConstants.CLIENT_API_KEY, postCallback);
                        break;
                     case CHANNEL_LIST:
                        apiInterface.getChannels(sessionId, getCallback);
                        break;
                     case PROGRAM_LIST:
                        String channelId = (String) parameterMap.get("channelId");
                        apiInterface.getPrograms(sessionId,channelId,getCallback);
                        break;
                  }

               } else {
                  reconnect(apiInterface, type, getCallback, postCallback);
               }
            }
         }
      });


   }


   public void checkTokenValid(String sessionId, final GetResponseCallback callback) {
      String restUrl = LiveTvConstants.API_URL;
      restUrl += "auth/check/" + sessionId;

      new GetTask(restUrl, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {
            callback.isValidToken(result);

         }
      }).execute();
   }

   private void reconnect(final ApiInterface apiInterface,
                          final MethodType type,
                          final GetResponseCallback getResponseCallback,
                          PostCallback postCallback) {
      switch (type) {
         case AUTH:
            auth(LiveTvConstants.APPLICATION_API_KEY, LiveTvConstants.CLIENT_API_KEY, postCallback);
            break;
         case CHANNEL_LIST:
            auth(LiveTvConstants.APPLICATION_API_KEY, LiveTvConstants.CLIENT_API_KEY, new PostCallback() {
               @Override
               public void onSessionIdReceived(String result) {

                  SharedPreferences prefs = getContext().getSharedPreferences(
                     "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
                  prefs.edit().putString(LiveTvConstants.SESSION_ID, result).apply();

                  apiInterface.getChannels(result, getResponseCallback);
               }
            });
            break;
      }

   }


   public Context getContext() {
      return context;
   }

   public void setContext(Context context) {
      this.context = context;
   }


   public void setSessionId(String sessionId) {

   }
}

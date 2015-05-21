package com.eatech.ceptv.application.api;

import android.content.Context;
import android.content.SharedPreferences;
import com.eatech.ceptv.bean.ApiUserRequest;
import com.eatech.ceptv.bean.StatRequest;
import com.eatech.ceptv.bean.UserRequest;
import com.eatech.ceptv.enums.StatType;
import com.eatech.ceptv.task.GetTask;
import com.eatech.ceptv.task.PostTask;
import com.eatech.ceptv.task.RestTaskCallback;
import com.eatech.ceptv.util.LiveTvConstants;
import com.eatech.ceptv.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author erhanasikoglu
 */
public class ApiApplication implements ApiInterface {

   private String sessionId;
   SharedPreferences prefs;


   private static class ApiApplicationHolderInstance {
      public static final ApiApplication INSTANCE = new ApiApplication();
   }

   public Context context;


   public static ApiApplication getInstance() {
      return ApiApplicationHolderInstance.INSTANCE;
   }


   public void authResource(String sessionId, final PostCallback callback) {

      checkAuthentication(sessionId, this, MethodType.AUTH, null, callback, null);

   }


   public void channelResource(String sessionId, GetResponseCallback callback) {


      checkAuthentication(sessionId, this, MethodType.CHANNEL_LIST, callback, null, null);
   }


   public void statResource(StatRequest request) {


      Map<String, StatRequest> parameterMap = new HashMap<String, StatRequest>();
      parameterMap.put("statRequest", request);
      checkAuthentication(sessionId, this, MethodType.STAT, null, null, parameterMap);
   }

   public void userResource(UserRequest request) {

      SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
      String exUserId = sharedPreferences.getString(LiveTvConstants.EX_USER_ID, null);

      if (ObjectUtil.isNull(exUserId) || "".equals(exUserId)) {

         Map<String, UserRequest> parameterMap = new HashMap<String, UserRequest>();
         parameterMap.put("userRequest", request);
         checkAuthentication(sessionId, this, MethodType.USER_SAVE, null, null, parameterMap);
      }
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
   public void postStat(StatRequest statRequest) {


      StatType statType = StatType.resolveFrom(statRequest.getStatType());

      String restUrl = LiveTvConstants.API_URL;
      restUrl += "stat/";
      String method = StatType.BROKEN.equals(statType) ? "broken" : "watched";

      restUrl += method + "?t=" + this.sessionId;


      new PostTask(restUrl, statRequest, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {
          }
      }).execute();
   }


   @Override
   public void postUser(UserRequest request) {
      String restUrl = LiveTvConstants.API_URL;
      restUrl += "user/save";

      restUrl += "?t=" + this.getSessionId();

      new PostTask(restUrl, request, new RestTaskCallback() {
         @Override
         public void onTaskComplete(String result) {
            SharedPreferences prefs = getContext().getSharedPreferences(
               "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
            prefs.edit().putString(LiveTvConstants.EX_USER_ID, result).apply();
         }
      }).execute();

   }

   @Override
   public void getPrograms(String sessionId, String channelId, final GetResponseCallback callback) {
      String restUrl = LiveTvConstants.API_URL;
      restUrl += "program/findByChannel/" + channelId + "?t=" + sessionId;

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
                     case STAT:
                        StatRequest statRequest = (StatRequest) parameterMap.get("statRequest");

                        apiInterface.postStat(statRequest);
                        break;

                     case USER_SAVE:
                        UserRequest userRequest = (UserRequest) parameterMap.get("userRequest");

                        apiInterface.postUser(userRequest);
                        break;

                  }

               } else {
                  reconnect(apiInterface, type, getCallback, postCallback, parameterMap);
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
                          PostCallback postCallback,
                          final Map parameterMap) {
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
         case STAT:
            auth(LiveTvConstants.APPLICATION_API_KEY, LiveTvConstants.CLIENT_API_KEY, new PostCallback() {
               @Override
               public void onSessionIdReceived(String result) {

                  SharedPreferences prefs = getContext().getSharedPreferences(
                     "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
                  prefs.edit().putString(LiveTvConstants.SESSION_ID, result).apply();
                  StatRequest request = (StatRequest) parameterMap.get("statRequest");
                  apiInterface.postStat(request);
               }
            });
            break;

         case USER_SAVE:
            auth(LiveTvConstants.APPLICATION_API_KEY, LiveTvConstants.CLIENT_API_KEY, new PostCallback() {
               @Override
               public void onSessionIdReceived(String result) {

                  SharedPreferences prefs = getContext().getSharedPreferences(
                     "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
                  prefs.edit().putString(LiveTvConstants.SESSION_ID, result).apply();
                  UserRequest request = (UserRequest) parameterMap.get("userRequest");
                  apiInterface.postUser(request);
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


   public String getSessionId() {
      prefs = this.getContext().getSharedPreferences(
         "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
      sessionId = prefs.getString(LiveTvConstants.SESSION_ID, null);
      return sessionId;
   }

   public void setSessionId(String sessionId) {
      this.sessionId = sessionId;
   }
}

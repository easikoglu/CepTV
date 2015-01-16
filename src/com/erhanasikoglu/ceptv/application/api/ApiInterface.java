package com.erhanasikoglu.ceptv.application.api;

/**
 * @author erhanasikoglu
 */
public interface ApiInterface {



   void auth(String applicationId,String clientId, PostCallback callback);


   void getChannels(String sessionId, GetResponseCallback callback);


   void getPrograms(String sessionId,String channelId,GetResponseCallback callback);
}

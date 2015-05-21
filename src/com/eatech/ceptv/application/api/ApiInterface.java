package com.eatech.ceptv.application.api;

import com.eatech.ceptv.bean.UserRequest;
import com.eatech.ceptv.bean.StatRequest;

/**
 * @author erhanasikoglu
 */
public interface ApiInterface {



   void auth(String applicationId,String clientId, PostCallback callback);


   void getChannels(String sessionId, GetResponseCallback callback);


   void getPrograms(String sessionId,String channelId,GetResponseCallback callback);


   void postUser(UserRequest request);

   void postStat(StatRequest statRequest);
}

package com.eatech.ceptv.bean;

import java.io.Serializable;

/**
 * @author erhanasikoglu
 */
public class StatRequest implements Serializable {



   private String userId;


   private Integer statType;

   private String channelId;

   private String channelUrl;

   private String channelName;


   public StatRequest() {
   }


   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public Integer getStatType() {
      return statType;
   }

   public void setStatType(Integer statType) {
      this.statType = statType;
   }

   public String getChannelId() {
      return channelId;
   }

   public void setChannelId(String channelId) {
      this.channelId = channelId;
   }


   public String getChannelUrl() {
      return channelUrl;
   }

   public void setChannelUrl(String channelUrl) {
      this.channelUrl = channelUrl;
   }

   public String getChannelName() {
      return channelName;
   }

   public void setChannelName(String channelName) {
      this.channelName = channelName;
   }
}

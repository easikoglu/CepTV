package com.erhanasikoglu.ceptv.bean;


import java.io.Serializable;

/**
 * @author erhanasikoglu
 */
 public class ApiUserRequest implements Serializable {


    private String applicationId;

    private String clientId;


   public ApiUserRequest() {
   }

   public ApiUserRequest(String applicationId, String clientId) {
       this.applicationId = applicationId;
      this.clientId = clientId;
   }

   public String getApplicationId() {
      return applicationId;
   }

   public void setApplicationId(String applicationId) {
      this.applicationId = applicationId;
   }

   public String getClientId() {
      return clientId;
   }

   public void setClientId(String clientId) {
      this.clientId = clientId;
   }
}

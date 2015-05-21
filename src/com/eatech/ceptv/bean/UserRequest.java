package com.eatech.ceptv.bean;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: eerhasi
 * Date: 04.12.2013
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
 public class UserRequest implements Serializable {


    private String name;

   private String surname;

    private String email;

    private String externalId;

   private String externalJson;


   public UserRequest() {
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getSurname() {
      return surname;
   }

   public void setSurname(String surname) {
      this.surname = surname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getExternalId() {
      return externalId;
   }

   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }

   public String getExternalJson() {
      return externalJson;
   }

   public void setExternalJson(String externalJson) {
      this.externalJson = externalJson;
   }
}

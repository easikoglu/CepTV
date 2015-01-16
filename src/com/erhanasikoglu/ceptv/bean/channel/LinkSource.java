package com.erhanasikoglu.ceptv.bean.channel;


import java.io.Serializable;

/**
 * @author erhanasikoglu
 */
 public class LinkSource implements Serializable {

   private String url;
   private String status;
   private String quality;
   private boolean webView = false;


   public LinkSource() {
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }


   public String getQuality() {
      return quality;
   }

   public void setQuality(String quality) {
      this.quality = quality;
   }

   public boolean isWebView() {
      return webView;
   }

   public void setWebView(boolean webView) {
      this.webView = webView;
   }


}

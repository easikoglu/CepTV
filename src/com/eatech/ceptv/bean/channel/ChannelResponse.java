package com.eatech.ceptv.bean.channel;

import java.io.Serializable;
import java.util.List;

/**
 * @author erhanasikoglu
 */
public class ChannelResponse implements Serializable {

   private String id;
   private String name;
   private List<LinkSource> urlList;
   private String logoPath;
   private List<ProgramResponse> programs;

   public ChannelResponse() {
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }


   public List<LinkSource> getUrlList() {
      return urlList;
   }

   public void setUrlList(List<LinkSource> urlList) {
      this.urlList = urlList;
   }

   public String getLogoPath() {
      return logoPath;
   }

   public void setLogoPath(String logoPath) {
      this.logoPath = logoPath;
   }


   public List<ProgramResponse> getPrograms() {
      return programs;
   }

   public void setPrograms(List<ProgramResponse> programs) {
      this.programs = programs;
   }
}

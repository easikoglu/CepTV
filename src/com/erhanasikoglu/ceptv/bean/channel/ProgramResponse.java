package com.erhanasikoglu.ceptv.bean.channel;

import com.erhanasikoglu.ceptv.util.ObjectUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author erhanasikoglu
 */
public class ProgramResponse implements Serializable, Comparable<ProgramResponse> {

   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

   private String name;
   private String type;
   private String summary;
   private String startDate;
   private String endDate;
    private String channelId;
   private boolean current;
   private String sDate;
   private String eDate;


   public ProgramResponse() {
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public String getStartDate() {
      return startDate;
   }

   public void setStartDate(String startDate) {
      this.startDate = startDate;
   }

   public String getEndDate() {
      return endDate;
   }

   public void setEndDate(String endDate) {
      this.endDate = endDate;
   }

   public String getChannelId() {
      return channelId;
   }

   public void setChannelId(String channelId) {
      this.channelId = channelId;
   }

   public boolean isCurrent() {
      return current;
   }

   public void setCurrent(boolean current) {
      this.current = current;
   }

   public String getsDate() {
      return sDate;
   }

   public void setsDate(String sDate) {
      this.sDate = sDate;
   }

   public String geteDate() {
      return eDate;
   }

   public void seteDate(String eDate) {
      this.eDate = eDate;
   }


   public Date getInDateStart() throws ParseException {

      return  dateFormat.parse(this.sDate);
   }


   public Date getInDateEnd() throws ParseException {

      if(ObjectUtil.isNotNull(this.eDate) && this.eDate!=""){
         return dateFormat.parse(this.eDate);
      }
      return  null;
   }

   @Override
   public int compareTo(ProgramResponse another) {
      try {
         return getInDateStart().compareTo(another.getInDateStart());
      } catch (ParseException e) {
         e.printStackTrace();
      }
      return 0;
   }
}

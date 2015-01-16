package com.erhanasikoglu.ceptv.bean.holder;

import java.util.HashMap;

/**
 * @author erhanasikoglu
 */
public class ProgramsCacheHolder extends HashMap<String, ProgramResponseHolder> {



   private static class CacheInstance {
      public static final ProgramsCacheHolder INSTANCE = new ProgramsCacheHolder();
   }


   public static ProgramsCacheHolder getInstance() {
      return CacheInstance.INSTANCE;
   }

}

package com.eatech.ceptv.enums;

/**
 * @author erhanasikoglu
 */
public enum StatType {

   BROKEN(99),WATCHED(100);


   Integer value;


   StatType(Integer value) {
      this.value = value;
   }

   public static StatType resolveFrom(Integer value) {

      StatType returnType = null;
      for (StatType statType : StatType.values()) {
         if (statType.getValue().equals(value)) {
            returnType = statType;
         }
      }
      return returnType;
   }

   public Integer getValue() {
      return value;
   }
}

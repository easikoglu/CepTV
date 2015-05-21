package com.eatech.ceptv.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.eatech.ceptv.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author erhanasikoglu
 */
public class LiveTvUtil {


   private static final java.lang.String STRING_TO_DATE = "HH:mm";

   public static boolean isConnected(Context context) {
      ConnectivityManager connectivityManager = (ConnectivityManager) context
                                                                         .getSystemService(Context.CONNECTIVITY_SERVICE);

      return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
   }

   public static AlertDialog showAlert(final Context context, String title,
                                       String message) {
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                         context);
      alertDialogBuilder.setTitle(title);
      alertDialogBuilder
         .setMessage(message)
         .setCancelable(false)
         .setPositiveButton(
                              context.getResources().getString(R.string.dialog_ok),
                              new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                 }
                              });

      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();

      return alertDialog;

   }

   public static boolean isMobileConnection(Context context) {
      ConnectivityManager connectivityManager = (ConnectivityManager) context
                                                                         .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo.State mobile = null;

      if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
         mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
      }

      return (mobile != null && mobile.equals(NetworkInfo.State.CONNECTED));
   }

   public static boolean isMobileOrWifiConneciton(Context context){

      ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

      if (mWifi.isAvailable() == false && mMobile.isAvailable() == false) {
         return false;
      }

      return true;
   }



   public static <T> T getObject(final String jsonString, final Class<T> objectClass) {
      Gson gson = new Gson();
      return gson.fromJson(jsonString, objectClass);
   }


   public static Date fromStringToDate(String str) throws ParseException {
      return new SimpleDateFormat(STRING_TO_DATE).parse(str);
   }



}

package com.erhanasikoglu.ceptv.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.application.api.ApiApplication;
import com.erhanasikoglu.ceptv.application.api.GetResponseCallback;
import com.erhanasikoglu.ceptv.application.api.PostCallback;
import com.erhanasikoglu.ceptv.util.ApiConnectUtil;
import com.erhanasikoglu.ceptv.util.LiveTvConstants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class MyApplication extends Application {

   private static final String TAG = "MyApplication";

   SharedPreferences prefs;

   @Override
   public void onCreate() {
      super.onCreate();
/*
         Parse.enableLocalDatastore(this);
*/
      Parse.initialize(this, "CcFtudDgNo5pzhUnIUJ0OqSCoo41leRCJMfD9aun",
         "9oLT9zjpLLPu8YHr4z1t7caoZrgVft4hIBwXsRhJ");
      ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

      ApiApplication apiApplication = ApiApplication.getInstance();

      apiApplication.setContext(this);


      apiApplication.authResource(getSessionId(), new PostCallback() {
         @Override
         public void onSessionIdReceived(String result) {
            setSessionId(result);
         }
      });

      /* unviersal image loader settings*/

      DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
      .cacheInMemory(true)
         .cacheOnDisk(true)
         .showImageOnLoading(R.drawable.loading)
         .showImageForEmptyUri(R.drawable.ceptvlogo128)
         .showImageOnFail(R.drawable.ceptvlogo128)
         .considerExifParams(true)
         .build();
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

      .defaultDisplayImageOptions(defaultOptions)
       .build();
      ImageLoader.getInstance().init(config);


   }

   public void setSessionId(String sessionId) {

      prefs = this.getSharedPreferences(
         "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
      prefs.edit().putString(LiveTvConstants.SESSION_ID,sessionId).apply();

   }

   public String getSessionId() {
      prefs = this.getSharedPreferences(
         "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
      return prefs.getString(LiveTvConstants.SESSION_ID,null);
   }


}
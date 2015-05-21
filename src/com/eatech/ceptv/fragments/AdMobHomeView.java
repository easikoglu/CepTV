package com.eatech.ceptv.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eatech.ceptv.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author erhanasikoglu
 */
public class AdMobHomeView extends RelativeLayout {

   private static final long SHOW_INTERVAL = 8000;

   private static final String AD_CLOSE_TEXT= " saniye...";

   private RelativeLayout adCloseTextArea;



   public AdMobHomeView(Context context) {
      super(context);
   }

   public AdMobHomeView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public AdMobHomeView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);

   }

   AdView mAdView;
   TextView closeText;


   public void makeView() {
      LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View mRoot = inflate.inflate(R.layout.fragment_ad, null);



      mAdView = (AdView) mRoot.findViewById(R.id.adView);
      AdRequest adRequest = new AdRequest.Builder()
         .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
         .build();

      // Start loading the ad in the background.
      mAdView.loadAd(adRequest);


      this.addView(mRoot);
   }






}

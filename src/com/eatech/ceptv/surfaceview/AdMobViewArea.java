package com.eatech.ceptv.surfaceview;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eatech.ceptv.R;
import com.eatech.ceptv.util.ObjectUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * @author erhanasikoglu
 */
public class AdMobViewArea extends RelativeLayout {

   private static final long SHOW_INTERVAL = 13000;

   private static final String AD_CLOSE_TEXT= " saniye...";

   private RelativeLayout adCloseTextArea;



   public AdMobViewArea(Context context) {
      super(context);
   }

   public AdMobViewArea(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public AdMobViewArea(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);

   }

   AdView mAdView;
   TextView closeText;


   public void makeView() {
      LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View mRoot = inflate.inflate(R.layout.fragment_ad, null);

      adCloseTextArea  = (RelativeLayout) mRoot.findViewById(R.id.adCloseTextArea);
      closeText = (TextView) mRoot.findViewById(R.id.adCloseText);



      DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();


      mAdView = (AdView) mRoot.findViewById(R.id.adView);
      AdRequest adRequest = new AdRequest.Builder()
         .build();

      // Start loading the ad in the background.
      mAdView.loadAd(adRequest);


      mAdView.setAdListener(new AdListener() {

         @Override
         public void onAdLoaded() {
            super.onAdLoaded();

            adCloseTextArea.setVisibility(VISIBLE);
         }
      });

      adCloseTextArea.setVisibility(GONE);

      this.addView(mRoot);
   }




   public void toggleView() {

      if (this.getVisibility() == View.GONE) {
         this.setVisibility(View.VISIBLE);
      } else {
         fadeOut(this);
      }
   }

   public void fadeOut(final View view) {

      Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
      fadeOut.setAnimationListener(new Animation.AnimationListener() {
         @Override
         public void onAnimationStart(Animation animation) {
            // Called when the Animation starts
         }

         @Override
         public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.GONE);
         }

         @Override
         public void onAnimationRepeat(Animation animation) {
            // This is called each time the Animation repeats
         }
      });

      view.startAnimation(fadeOut);
   }

    public CountDownTimer adMobTimer = new CountDownTimer(SHOW_INTERVAL, 1000) {

      public void onTick(long leftTimeInMilliseconds) {


         if(adCloseTextArea.getVisibility() == VISIBLE){
            long seconds = leftTimeInMilliseconds / 1000;
              String text = seconds +  AD_CLOSE_TEXT;
            closeText.setText(text);
         }
       }

      public void onFinish() {
         if(ObjectUtil.isNotNull(this)) {
            setVisibility(View.GONE);
         }
      }
   };

}

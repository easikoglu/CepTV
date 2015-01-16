package com.erhanasikoglu.ceptv.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.application.api.ApiApplication;
import com.erhanasikoglu.ceptv.bean.channel.ChannelResponse;
import com.erhanasikoglu.ceptv.bean.channel.LinkSource;
import com.erhanasikoglu.ceptv.surfaceview.ActionViewArea;
import com.erhanasikoglu.ceptv.surfaceview.AdMobViewArea;
import com.erhanasikoglu.ceptv.util.LiveTvUtil;
import com.erhanasikoglu.ceptv.util.ObjectUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.parse.ParseObject;
import com.parse.ParseUser;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.*;

public class VitamioCustomPlayer extends Activity implements OnBufferingUpdateListener,
   OnCompletionListener,
   OnPreparedListener,
   OnErrorListener,
   OnVideoSizeChangedListener,
   SurfaceHolder.Callback,
   SurfaceView.OnTouchListener {

   private static final String TAG = "MediaPlayerDemo";
   private static final long BANNER_INTERVALS = 50000;
   private static final long INTERSTITIAL_INTERVALS = 3000;


   private int mVideoWidth;

   private int mVideoHeight;

   private MediaPlayer mMediaPlayer;

   private SurfaceView mPreview;

   private SurfaceHolder holder;

   private Bundle extras;


   private boolean mIsVideoSizeKnown = false;

   private boolean mIsVideoReadyToBePlayed = false;


   private String currentUrl;


   private String url;

   private String urlHD;

   private String channelId;

   private String channelName;

   private FrameLayout surfaceContainer;

   private boolean touched;

   private ActionViewArea actionViewArea;

   private AdMobViewArea adMobViewArea;

   private boolean firstStart;

   private ProgressDialog progress;


   private String sessionId;

   final ApiApplication apiApplication = ApiApplication.getInstance();

   ChannelResponse channelObject;

   /*interstitial admob variables*/
   private PublisherInterstitialAd mInterstitialAd;

   PublisherAdRequest publisherAdRequest;


   @Override
   public void onCreate(Bundle icicle) {
      super.onCreate(icicle);


//      this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

      //Remove notification bar
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      setContentView(R.layout.vitamio_player);
      mPreview = (SurfaceView) findViewById(R.id.vitamio_surface);


      surfaceContainer = (FrameLayout) findViewById(R.id.videoSurfaceContainer);


      holder = mPreview.getHolder();
      holder.addCallback(this);
      holder.setFormat(PixelFormat.RGBA_8888);
      extras = getIntent().getExtras();


      channelObject = (ChannelResponse) getIntent().getSerializableExtra("channelObject");

      firstStart = extras.getBoolean("firstStart");

      sessionId = extras.getString("sessionId");


      channelName = channelObject.getName();
      channelId = channelObject.getId();


      for (LinkSource source : channelObject.getUrlList()) {

         if (source.getStatus().equals("active")) {
            url = source.getUrl();
            break;
         }
      }


      currentUrl = url;

      prepareProgress();


      mPreview.setOnTouchListener(this);

      apiApplication.setContext(this);

   }


   private void prepareProgress() {
      this.progress = new ProgressDialog(this);
      this.progress.setCancelable(false);
      this.progress.setCanceledOnTouchOutside(false);
      this.progress.setMessage(getString(R.string.channel_in_progress));
   }


   private void playVideo() {
      doCleanUp();
      try {

         // Create a new media player and set the listeners
         mMediaPlayer = new MediaPlayer(this);
         mMediaPlayer.setDataSource(url);
         mMediaPlayer.setDisplay(holder);
         mMediaPlayer.prepareAsync();
         mMediaPlayer.setOnBufferingUpdateListener(this);
         mMediaPlayer.setOnCompletionListener(this);
         mMediaPlayer.setOnPreparedListener(this);
         mMediaPlayer.setOnErrorListener(this);
         mMediaPlayer.setOnVideoSizeChangedListener(this);
         setVolumeControlStream(AudioManager.STREAM_MUSIC);

      } catch (Exception e) {
         Log.e(TAG, "error: " + e.getMessage(), e);
      }
   }

   public void onBufferingUpdate(MediaPlayer arg0, int percent) {

   }

   public void onCompletion(MediaPlayer arg0) {
      Log.d(TAG, "onCompletion called");
   }

   public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
      Log.v(TAG, "onVideoSizeChanged called");
      if (width == 0 || height == 0) {
         Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
         return;
      }

      Log.e(TAG, "video sizes width : (" + width + ") and height(" + height + ")");

      mIsVideoSizeKnown = true;
      mVideoWidth = width;
      mVideoHeight = height;

   }

   public void onPrepared(MediaPlayer mediaplayer) {
      Log.d(TAG, "onPrepared called");
      mIsVideoReadyToBePlayed = true;

      if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
         startVideoPlayback();
      }
      createAdMobViewArea();


      if (isFirstStart() && ObjectUtil.isNotNull(progress)) {
         progress.dismiss();
         firstStart = false;
      }

   }

   public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
      Log.d(TAG, "surfaceChanged called");


   }

   public void surfaceDestroyed(SurfaceHolder surfaceholder) {
      Log.d(TAG, "surfaceDestroyed called");
   }

   public void surfaceCreated(SurfaceHolder holder) {
      if (isFirstStart()) {
         if (ObjectUtil.isNotNull(progress)) {
            progress.show();
         }
      }
      Log.d(TAG, "surfaceCreated called");
      playVideo();
      createChannelViewArea();

      Log.d(TAG, "streaming starting...");

      logChannelUsage();
   }

   private void logChannelUsage() {

      ParseUser user = ParseUser.getCurrentUser();
      String userId = "anonim";
      if (ObjectUtil.isNotNull(user)) {
         userId = user.getObjectId();
      }
      ParseObject object = new ParseObject("Statistic");
      object.put("USER_ID", userId);
      object.put("CHANNEL_ID", channelId);
      object.put("CHANNEL_NAME", channelName);

      object.saveEventually();
   }


   private void createAdMobViewArea() {

      if (ObjectUtil.isNull(adMobViewArea)) {
         adMobViewArea = new AdMobViewArea(this);
         adMobViewArea.makeView();
         surfaceContainer.addView(adMobViewArea);
         adMobViewArea.setVisibility(View.GONE);
         startTriggerThread();
      }

   }

   private void createChannelViewArea() {
      if (ObjectUtil.isNull(actionViewArea)) {
         actionViewArea = new ActionViewArea(this, surfaceContainer);
         actionViewArea.setSelectedChannelId(channelId);
         actionViewArea.setChannelObject(channelObject);
         actionViewArea.makeView();
         surfaceContainer.addView(actionViewArea);
      }
   }


   @Override
   public void onPause() {
      super.onPause();
      releaseMediaPlayer();
      doCleanUp();
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      releaseMediaPlayer();
      doCleanUp();
   }

   public void stopPlayback() {
      if (mMediaPlayer != null) {
         mMediaPlayer.stop();
         mMediaPlayer.release();
         mMediaPlayer = null;
      }
   }


   private void releaseMediaPlayer() {
      if (mMediaPlayer != null) {
         mMediaPlayer.release();
         mMediaPlayer = null;
      }
   }

   private void doCleanUp() {
      mVideoWidth = 0;
      mVideoHeight = 0;
      mIsVideoReadyToBePlayed = false;
      mIsVideoSizeKnown = false;
   }

   private void startVideoPlayback() {
      Log.v(TAG, "startVideoPlayback");
      mMediaPlayer.start();

   }

   @Override
   public void onBackPressed() {

      /*interstitial admob initiliaze*/
      this.loadInterstitialAdMob();


      //    finish();

   }

   public void wrappedBackPress() {
      super.onBackPressed();
   }

   @Override
   public boolean onTouch(View v, MotionEvent event) {


      int action = event.getAction();

      switch (action) {
         case MotionEvent.ACTION_DOWN:
            if (ObjectUtil.isNotNull(actionViewArea)) {
               actionViewArea.toggleView();
               actionViewArea.closePopUpViews();
            }
            touched = true;
            break;
         case MotionEvent.ACTION_MOVE:
            touched = true;
            break;
         case MotionEvent.ACTION_UP:
            touched = false;
            break;
         case MotionEvent.ACTION_CANCEL:
            touched = false;
            break;
         case MotionEvent.ACTION_OUTSIDE:
            touched = false;
            break;
         default:
      }


      return false;
   }


   public void changeSource(ChannelResponse object) {


      Log.d(TAG, "advertising showing...");

      //displayInterstitial();

      if (ObjectUtil.isNotNull(object.getUrlList()) && object.getUrlList().size() > 0) {
         for (LinkSource source : object.getUrlList()) {

            if (source.getStatus().equals("active")) {
               this.url = source.getUrl();

               this.channelId = object.getId();

               this.channelName = object.getName();

               this.stopPlayback();
               this.playVideo();
               break;
               //mInterstitialAd.loadAd(publisherAdRequest);

            } else {

               //error occured
               Log.d(TAG, "channel url-error...");
            }
         }
      } else {
         LiveTvUtil.showAlert(this, "Video adres hatası",
            "Hata keydedildi. En kısa sürede çözülecektir.");

         //TODO log error

      }


   }


   @Override
   public boolean onError(MediaPlayer mp, int what, int extra) {


      if (isFirstStart() && ObjectUtil.isNotNull(progress)) {
         progress.dismiss();
         firstStart = false;
      }
      //logChannelErrors();

      AlertDialog ad = new AlertDialog.Builder(this)
         .create();
      ad.setCancelable(false);
      ad.setTitle("Video Adres Hatası");
      ad.setMessage("Hata keydedildi. En kısa sürede çözülecektir. Lütfen diğer kanallar için \"Tamam\" tuşuna basınız.");
      ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
         @Override
         public void onDismiss(DialogInterface dialog) {
            wrappedBackPress();
         }
      });
      ad.setButton(this.getString(R.string.ok_text), new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (ObjectUtil.isNotNull(actionViewArea)) {
               actionViewArea.toggleView();
               actionViewArea.closePopUpViews();
            }
            wrappedBackPress();

         }
      });
      ad.show();
      return true;
   }

   private void logChannelErrors() {


      ParseObject object = new ParseObject("BrokenLinks");
      object.put("CHANNEL_ID", channelId);
      object.put("CHANNEL_NAME", channelName);
      object.put("CHANNEL_URL", currentUrl);
      object.saveEventually();

   }

   public void pause() {

      if (ObjectUtil.isNotNull(mMediaPlayer)) {
         if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
         }
      }
   }

   public void start() {
      if (ObjectUtil.isNotNull(mMediaPlayer)) {
         if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
         }
      }
   }


   public void stopStart() {

      if (ObjectUtil.isNotNull(mMediaPlayer)) {
         if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
         } else if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
         }
      }
   }

   public boolean isPlaying() {

      return mMediaPlayer != null ? mMediaPlayer.isPlaying() : false;
   }

   public boolean isFirstStart() {
      return firstStart;
   }

   public void setFirstStart(boolean firstStart) {
      this.firstStart = firstStart;
   }


   private Handler handler = new Handler() {
      public void handleMessage(Message msg) {
         adMobViewArea.setVisibility(msg.what);
      }
   };

   private void startTriggerThread() {
      new Thread() {
         boolean show = false;

         public void run() {
            while (true) {


               if (show) {
                  handler.sendEmptyMessage(View.GONE);
                  adMobViewArea.adMobTimer.cancel();
                  show = false;
               } else {
                  handler.sendEmptyMessage(View.VISIBLE);
                  adMobViewArea.adMobTimer.start();
                  show = true;
               }
               try {
                  Thread.sleep(BANNER_INTERVALS);
               } catch (InterruptedException e) {
                  // Ignore.
               }
            }
         }
      }.start();
   }


   public void loadInterstitialAdMob() {

      mInterstitialAd = new PublisherInterstitialAd(this);
      mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

      publisherAdRequest = new PublisherAdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();


      mInterstitialAd.loadAd(publisherAdRequest);


      mInterstitialAd.setAdListener(new AdListener() {
         public void onAdLoaded() {
            super.onAdLoaded();
            Log.i(TAG, "intersitital loaded...");
            if (mInterstitialAd.isLoaded()) {
               mInterstitialAd.show();
            }
         }

         @Override
         public void onAdClosed() {
            super.onAdClosed();
            wrappedBackPress();
         }

         @Override
         public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            wrappedBackPress();
         }
      });
   }


}

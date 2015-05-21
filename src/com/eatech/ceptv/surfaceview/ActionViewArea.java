package com.eatech.ceptv.surfaceview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.eatech.ceptv.adapter.items.ChannelItem;
import com.eatech.ceptv.bean.channel.ProgramResponse;
import com.eatech.ceptv.task.ChangeChannelAsyncTask;
import com.eatech.ceptv.R;
import com.eatech.ceptv.activity.VitamioCustomPlayer;
import com.eatech.ceptv.adapter.ChannelControllerAdapter;
import com.eatech.ceptv.adapter.ProgramListAdapter;
import com.eatech.ceptv.adapter.items.ProgramItem;
import com.eatech.ceptv.bean.channel.ChannelResponse;
import com.eatech.ceptv.bean.channel.LinkSource;
import com.eatech.ceptv.bean.holder.ChannelResponseHolder;
import com.eatech.ceptv.util.LiveTvUtil;
import com.eatech.ceptv.util.ObjectUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author erhanasikoglu
 */
public class ActionViewArea extends RelativeLayout {


   private static final String TAG = "ChannelViewArea";

   int viewWidth;

   int viewHeight;

   private ProgressDialog volumeChangeProgress;

   private AudioManager audioManager;

   private String selectedChannelId;

   private Button tvControlBtn;

   private Button mediaControlBtn;

   private Button volumeControlBtn;

   private Button programsControlBtn;

   private LinearLayout volumeArea;

   private LinearLayout channelListArea;

   private LinearLayout programsListArea;

   private ListView channelListView;

   private ListView programsListView;

   private List<LinearLayout> popUpList;

   private FrameLayout surfaceContainer;

   private ChannelResponse channelObject;

   ProgramListAdapter programListAdapter;

   View mRoot;

   ChannelControllerAdapter channelControllerAdapter;

   private Integer selectedPosition = 0;




   public ActionViewArea(Activity activity, FrameLayout surfaceContainer) {
      super(activity);
      this.popUpList = new ArrayList<LinearLayout>();
      this.surfaceContainer = surfaceContainer;


   }





   public void makeView() {
      LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      mRoot = inflate.inflate(R.layout.interactive_area, null);
      DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
      int viewHeight = metrics.heightPixels;
      int viewWidth = metrics.widthPixels;
      final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewWidth, viewHeight, Gravity.LEFT);
      mRoot.setLayoutParams(params);
      initVolumeBar(mRoot);
      initChannelList(mRoot);

      initBtnSurface(mRoot);
      this.setVisibility(View.GONE);

      this.initProgramsList();


      this.addView(mRoot);
   }

   private void initBtnSurface(View mRoot) {

      tvControlBtn = (Button) mRoot.findViewById(R.id.tv_control_btn);

      mediaControlBtn = (Button) mRoot.findViewById(R.id.media_control_btn);

      volumeControlBtn = (Button) mRoot.findViewById(R.id.volume_control_btn);

      programsControlBtn = (Button) mRoot.findViewById(R.id.programs_control_btn);

      mediaControlBtn.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {


            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               if ((getContext() instanceof VitamioCustomPlayer)) {
                  ((VitamioCustomPlayer) getContext()).stopStart();
               }
               if (!((VitamioCustomPlayer) getContext()).isPlaying()) {
                  v.setBackgroundResource(R.drawable.media_control_btn_paused);
               } else {
                  v.setBackgroundResource(R.drawable.media_control_btn);
               }
            }
            return false;
         }
      });

      volumeControlBtn.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               if (volumeArea.getVisibility() == View.GONE) {
                  volumeArea.setVisibility(View.VISIBLE);
                  closeOpenedViews(volumeArea);
               } else {
                  fadeOut(volumeArea);
               }
            }
            return false;
         }
      });

      tvControlBtn.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               if (channelListArea.getVisibility() == View.GONE) {

                  bottomToUp(channelListArea);
                  closeOpenedViews(channelListArea);



               } else {
                  fadeOut(channelListArea);
               }
            }
            return false;
         }
      });

      programsControlBtn.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {


            } else if (event.getAction() == MotionEvent.ACTION_UP) {
               if (programsListArea.getVisibility() == View.GONE) {

                  bottomToUp(programsListArea);
                  closeOpenedViews(programsListArea);
               } else {
                  fadeOut(programsListArea);
               }
            }
            return false;
         }
      });

   }

   private void initChannelList(final View mRoot) {
      DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
      viewWidth = metrics.widthPixels;
      viewHeight = metrics.heightPixels;

      channelListArea = (LinearLayout) mRoot.findViewById(R.id.channel_list_layout);
      popUpList.add(channelListArea);


      channelListView = (ListView) mRoot.findViewById(R.id.channels);


      final ChannelResponseHolder holder = ChannelResponseHolder.getInstance();

      channelControllerAdapter = new ChannelControllerAdapter(createMenuItemList(holder.getChannelResponseList()),
         getContext(),
         selectedChannelId
      );


      channelListView.setAdapter(channelControllerAdapter);




      Button closeBtn = (Button) mRoot.findViewById(R.id.tv_bar_close_btn);

      closeBtn.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            fadeOut(channelListArea);
         }
      });


   }


   public void initProgramsList() {


      DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
      viewWidth = metrics.widthPixels;
      viewHeight = metrics.heightPixels;

      programsListArea = (LinearLayout) mRoot.findViewById(R.id.programs_list_layout);
      popUpList.add(programsListArea);


      programsListView = (ListView) mRoot.findViewById(R.id.programs);


      refreshProgramList();

      Button closeBtn = (Button) mRoot.findViewById(R.id.program_bar_close_btn);

      closeBtn.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            fadeOut(programsListArea);
         }
      });


   }


   public void refreshProgramList() {

      if (ObjectUtil.isNotNull(channelObject.getPrograms())) {


         List<ProgramResponse> programs = channelObject.getPrograms();
         Date now = new Date();
         int index = 0;
         for (ProgramResponse program : programs) {

            if (programs.size() > index + 1) {
               try {
                  if (program.getInDateStart().before(now) && programs.get(index + 1).getInDateStart().after(now)) {
                     program.setCurrent(true);

                  }
                  program.setEndDate(programs.get(index + 1).getStartDate());
                  program.seteDate(programs.get(index + 1).getsDate());
               } catch (ParseException e) {

                  e.printStackTrace();
               }
            }
            index++;
         }
         Collections.sort(channelObject.getPrograms());
         List<ProgramItem> programList = this.createProgramList(channelObject.getPrograms());

         if (programListAdapter != null) {
            programListAdapter.clear();
            programListAdapter.addAll(programList);
            programListAdapter.notifyDataSetChanged();
         } else {
            programListAdapter = new ProgramListAdapter(programList, getContext());
         }
         programsListView.setAdapter(programListAdapter);

         //programsListView.smoothScrollToPosition(programListAdapter.getSelectedItemPosition());

         programsControlBtn.setVisibility(VISIBLE);
      } else {
         programsControlBtn.setVisibility(GONE);
      }


   }


   public List<ProgramItem> createProgramList(List<ProgramResponse> list) {
      List<ProgramItem> menuList = new ArrayList<ProgramItem>();

      for (ProgramResponse obj : list) {
         ProgramItem program = new ProgramItem(obj);
         menuList.add(program);
      }
      return menuList;

   }

   private void bottomToUp(final View view) {

      Animation bottomToUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
      bottomToUp.setAnimationListener(new Animation.AnimationListener() {
         @Override
         public void onAnimationStart(Animation animation) {
            view.setVisibility(View.VISIBLE);
         }

         @Override
         public void onAnimationEnd(Animation animation) {

         }

         @Override
         public void onAnimationRepeat(Animation animation) {

         }
      });

      view.startAnimation(bottomToUp);
   }


   private void fadeOut(final View view) {

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


   public void initVolumeBar(View mRoot) {


      audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
      int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

      volumeArea = (LinearLayout) mRoot.findViewById(R.id.volume_bar_layout);
      popUpList.add(volumeArea);
      SeekBar volControl = (SeekBar) mRoot.findViewById(R.id.volume_bar);
      final TextView textView = (TextView) mRoot.findViewById(R.id.volume_text);
      Button closeBtn = (Button) mRoot.findViewById(R.id.volume_bar_close_btn);
      volControl.setMax(maxVolume);
      volControl.setProgress(curVolume);

      final String volText = "Ses:%";
      if (curVolume != 15) {
         textView.setText(volText + (100 / 15 + 1) * curVolume);
      } else {
         textView.setText(volText + "100");

      }
      volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

         @Override
         public void onStopTrackingTouch(SeekBar arg0) {
            // TODO Auto-generated method stub

         }

         @Override
         public void onStartTrackingTouch(SeekBar arg0) {

         }

         @Override
         public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            // TODO Auto-generated method stub
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            String volumeText = null;
            if (arg1 != 15) {
               volumeText = volText + (100 / 15 + 1) * arg1;
            } else {
               volumeText = volText + "100";
            }
            if (ObjectUtil.isNotNull(textView)) {
               textView.setText(volumeText);
            }

         }
      });

      closeBtn.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            fadeOut(volumeArea);
         }
      });

   }


   public String getSelectedChannelId() {
      return selectedChannelId;
   }

   public void setSelectedChannelId(String selectedChannelId) {
      this.selectedChannelId = selectedChannelId;
   }

   public void setChangeScreenBrightness() {

      Settings.System.putInt(getContext().getContentResolver(),
         Settings.System.SCREEN_BRIGHTNESS, 20);
      WindowManager.LayoutParams lp = ((VitamioCustomPlayer) getContext()).getWindow().getAttributes();
      lp.screenBrightness = 0.2f;// 100 / 100.0f;
      ((VitamioCustomPlayer) getContext()).getWindow().setAttributes(lp);
   }

   public ArrayList<ChannelItem> createMenuItemList(List<ChannelResponse> channelList) {
      ArrayList<ChannelItem> menuList = new ArrayList<ChannelItem>();

      for (ChannelResponse obj : channelList) {
         ChannelItem channel = new ChannelItem(ChannelItem.ChannelItemType.MENU_CHANNEL, obj);
         channel.setOnClickListener(createClickListener(ChannelItem.ChannelActionType.CHANGE_CHANNEL, obj));

         menuList.add(channel);

         if(channel.getChannelResponse().getId().equals(selectedChannelId)){
            selectedPosition = menuList.indexOf(channel);
         }
      }
      return menuList;

   }

   public void toggleView() {

      if (this.getVisibility() == View.GONE) {
         this.setVisibility(View.VISIBLE);
      } else {
         fadeOut(this);
      }
   }


   private OnClickListener createClickListener(final ChannelItem.ChannelActionType type, final ChannelResponse object) {

      return new OnClickListener() {

         @Override
         public void onClick(View v) {

            Log.d(TAG, "action type : " + type);

            switch (type) {

               case CHANGE_CHANNEL:
                   boolean source = changeSource(object);
                  if (source) {
                     refreshOtherChannels();
                     v.setBackgroundResource(R.drawable.menulabelchoosen);
                     selectedChannelId = object.getId();

                  }
                  break;

               case CLOSE:
                  break;


            }
         }
      };


   }

   public boolean changeSource(ChannelResponse object) {


      if(!object.getId().equals(channelObject.getId())) {

         if (ObjectUtil.isNotNull(object.getUrlList()) && object.getUrlList().size() > 0) {

            boolean found = false;
            for(LinkSource ls : object.getUrlList()){
               if(ls.getStatus().equals("active")){
                  found = true;
                  break;
               }
            }


            if (found  && (getContext() instanceof VitamioCustomPlayer)) {

               new ChangeChannelAsyncTask(getContext(), object, this).execute();
            }else {
               LiveTvUtil.showAlert(getContext(), "Video adres hatası",
                  "Hata keydedildi. En kısa sürede çözülecektir.");

               ((VitamioCustomPlayer) getContext()).logBroken(object.getName(), null, object.getId());
            }
            return true;
         } else {
            LiveTvUtil.showAlert(getContext(), "Video adres hatası",
               "Hata keydedildi. En kısa sürede çözülecektir.");

            ((VitamioCustomPlayer) getContext()).logBroken(object.getName(), null, object.getId());
         }
      }

      return false;
   }

   private void refreshOtherChannels() {

      int childCount = channelListView.getChildCount();

      for (int i = 0; i < childCount; i++) {
         View v = channelListView.getChildAt(i);
         v.setBackgroundResource(R.drawable.menulabel);
      }
   }

   public void closePopUpViews() {

      if (ObjectUtil.isNotNull(volumeArea)) {
         volumeArea.setVisibility(View.GONE);
      }
      if (ObjectUtil.isNotNull(channelListArea)) {
         channelListArea.setVisibility(View.GONE);
      }
      if (ObjectUtil.isNotNull(programsListArea)) {
         programsListArea.setVisibility(View.GONE);
      }
   }

   public void closeOpenedViews(LinearLayout object) {
      if (ObjectUtil.isNotNull(popUpList) && !popUpList.isEmpty()) {
         for (LinearLayout layout : popUpList) {
            if (!object.equals(layout)) {
               layout.setVisibility(View.GONE);
            }
         }
      }
   }


   public void addToFavorites() {


      this.getSelectedChannelId();
   }

   public void removeFromFavorites() {


   }

   public ChannelResponse getChannelObject() {
      return channelObject;
   }

   public void setChannelObject(ChannelResponse channelObject) {
      this.channelObject = channelObject;
   }
}

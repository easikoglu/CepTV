package com.eatech.ceptv.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.eatech.ceptv.adapter.items.ChannelItem;
import com.eatech.ceptv.R;
import com.eatech.ceptv.activity.MainActivity;
import com.eatech.ceptv.activity.VitamioCustomPlayer;
import com.eatech.ceptv.adapter.ChannelMainListAdapter;
import com.eatech.ceptv.application.api.ApiApplication;
import com.eatech.ceptv.application.api.GetResponseCallback;
import com.eatech.ceptv.bean.StatRequest;
import com.eatech.ceptv.bean.channel.ChannelResponse;
import com.eatech.ceptv.bean.channel.LinkSource;
import com.eatech.ceptv.bean.holder.ChannelResponseHolder;
import com.eatech.ceptv.enums.StatType;
import com.eatech.ceptv.util.Connectivity;
import com.eatech.ceptv.util.LiveTvUtil;
import com.eatech.ceptv.util.ObjectUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeView extends Fragment {


   private static final String TAG = HomeView.class.getName();

   private ListView channelListView;
   Context context;

   private String sessionId;

   final ApiApplication apiApplication = ApiApplication.getInstance();


   private String urls[] = new String[10];


   TextView loadingBar;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      this.context = this.getActivity();


      View v = inflater.inflate(R.layout.home_view, container, false);

      //wifi control
      TextView tryAgainText = (TextView) v.findViewById(R.id.try_again_text);
      Button wifiControlBtn = (Button) v.findViewById(R.id.wifi_control_btn);

      channelListView = (ListView) v.findViewById(R.id.channel_list);

      loadingBar = (TextView) v.findViewById(R.id.loading_bar);

      if (Connectivity.isConnected(getActivity())) {


         apiApplication.setContext(getActivity());

         sessionId = ((MainActivity) getActivity()).getSessionId();

         apiApplication.channelResource(sessionId, new GetResponseCallback() {
            @Override
            public void onChannelListReceived(String result) {

               if (result != null && !"".equals(result)) {

                  JsonParser jsonParser = new JsonParser();
                  JsonObject jsonObject = (JsonObject) jsonParser.parse(result);
                  Gson gson = new Gson();

                  Type listType = new TypeToken<List<ChannelResponse>>() {
                  }.getType();
                  List<ChannelResponse> items = gson.fromJson(jsonObject.get("response"), listType);

                  if (ObjectUtil.isNotNull(items) && items.size() > 0) {

                     ChannelResponseHolder holder = ChannelResponseHolder.getInstance();
                     holder.setChannelResponseList(items);
                     populateMainLayout(items);
                  }
               }
            }
         });


         AdView mAdView = (AdView) v.findViewById(R.id.adViewHome);
         AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build();

         // Start loading the ad in the background.
         mAdView.loadAd(adRequest);

      } else { //no internet conneciton


         loadingBar.setText("Bağlantı Yok!");
         tryAgainText.setVisibility(View.VISIBLE);
         wifiControlBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if (event.getAction() == MotionEvent.ACTION_UP) {

                  if (getActivity() instanceof MainActivity) {
                     ((MainActivity) getActivity()).startHomeView();
                  }

               }
               return false;
            }
         });

         wifiControlBtn.setVisibility(View.VISIBLE);
         LiveTvUtil.showAlert(getActivity(), "Bağlantı Hatası", "Lütfen internet bağlantınızı kontrol ediniz.");

      }


      return v;
   }


   @Override
   public void onResume() {
      super.onResume();
   }

   @Override
   public void onPause() {
      super.onPause();

   }

   @Override
   public void onStop() {
      super.onStop();
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
   }


   public void populateMainLayout(List<ChannelResponse> channelList) {

      List<ChannelItem> items = createChanelList(channelList);
      final ChannelMainListAdapter channelAdapter = new ChannelMainListAdapter(items, context);
      channelListView.setAdapter(channelAdapter);
      loadingBar.setVisibility(View.GONE);
      channelListView.setVisibility(View.VISIBLE);


      boolean pauseOnScroll = false; // or true
      boolean pauseOnFling = true; // or false
      PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
      channelListView.setOnScrollListener(listener);
   }


   public List<ChannelItem> createChanelList(List<ChannelResponse> channelList) {
      List<ChannelItem> menuList = new ArrayList<ChannelItem>();

      for (ChannelResponse obj : channelList) {
         ChannelItem channel = new ChannelItem(ChannelItem.ChannelItemType.MENU_CHANNEL, obj);

         channel.setOnTouchListener(createChannelTouchListener(obj));
         /*channel.setOnClickListener(createChannelClickListener(ChannelItem.ChannelActionType.CHANGE_CHANNEL, obj));*/
         menuList.add(channel);
      }
      return menuList;

   }


   private View.OnTouchListener createChannelTouchListener(final ChannelResponse object) {


      return new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {


            } else if (event.getAction() == MotionEvent.ACTION_UP) {

               v.setBackgroundColor(getResources().getColor(R.color.basicBckgrnd));
               if (ObjectUtil.isNotNull(object.getUrlList())
                  && object.getUrlList().size() > 0) {
                  boolean found = false;
                  for (LinkSource ls : object.getUrlList()) {
                     if (ls.getStatus().equals("active")) {
                        found = true;
                        break;
                     }
                  }
                  if (found) {

                     logChannelUsage(object);

                     v.setBackgroundResource(R.drawable.menulabelchoosen);
                     Intent intent = new Intent(getActivity(), VitamioCustomPlayer.class);

                     intent.putExtra("channelObject", object);
                     intent.putExtra("firstStart", true);
                     intent.putExtra("sessionId", sessionId);
                     startActivity(intent);
                  } else {
                     LiveTvUtil.showAlert(getActivity(), "Video adres hatası",
                        "Hata keydedildi. En kısa sürede çözülecektir.");
                     logBroken(object, "not active");
                  }

               } else {
                  LiveTvUtil.showAlert(getActivity(), "Video adres hatası",
                     "Hata keydedildi. En kısa sürede çözülecektir.");
                  logBroken(object, null);
               }
            }
            return false;
         }
      };
   }


   private void logChannelUsage(ChannelResponse object) {

      StatRequest statRequest = new StatRequest();

      statRequest.setChannelId(object.getId());
      statRequest.setChannelName(object.getName());
      statRequest.setChannelUrl(null);
      statRequest.setStatType(StatType.WATCHED.getValue());


      apiApplication.statResource(statRequest);
   }


   public void logBroken(ChannelResponse object, String url) {


      StatRequest statRequest = new StatRequest();

      statRequest.setChannelId(object.getId());
      statRequest.setChannelName(object.getName());
      statRequest.setChannelUrl(url);
      statRequest.setStatType(StatType.BROKEN.getValue());
      apiApplication.statResource(statRequest);


   }


}

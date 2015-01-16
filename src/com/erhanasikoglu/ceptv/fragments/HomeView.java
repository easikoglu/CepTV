package com.erhanasikoglu.ceptv.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.activity.MainActivity;
import com.erhanasikoglu.ceptv.activity.VitamioCustomPlayer;
import com.erhanasikoglu.ceptv.adapter.ChannelMainListAdapter;
import com.erhanasikoglu.ceptv.adapter.items.ChannelItem;
import com.erhanasikoglu.ceptv.application.api.ApiApplication;
import com.erhanasikoglu.ceptv.application.api.GetResponseCallback;
import com.erhanasikoglu.ceptv.bean.channel.ChannelResponse;
import com.erhanasikoglu.ceptv.bean.channel.LinkSource;
import com.erhanasikoglu.ceptv.bean.holder.ChannelResponseHolder;
import com.erhanasikoglu.ceptv.util.Connectivity;
import com.erhanasikoglu.ceptv.util.LiveTvUtil;
import com.erhanasikoglu.ceptv.util.ObjectUtil;
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


         final ApiApplication apiApplication = ApiApplication.getInstance();

         apiApplication.setContext(getActivity());

         sessionId = ((MainActivity) getActivity()).getSessionId();
         final String applicationId = ((MainActivity) getActivity()).getApplicationId();
         final String clientId = ((MainActivity) getActivity()).getClientId();

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


   private View.OnTouchListener createChannelTouchListener(final ChannelResponse object){


      return new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_UP) {

               if (ObjectUtil.isNotNull(object.getUrlList())
                  && object.getUrlList().size() > 0) {
                  boolean found = false;
                  for(LinkSource ls : object.getUrlList()){
                     if(ls.getStatus().equals("active")){
                        found = true;
                        break;
                     }
                  }
                  if(found){

                     v.setBackgroundResource(R.drawable.menulabelchoosen);
                     Intent intent = new Intent(getActivity(), VitamioCustomPlayer.class);

                     intent.putExtra("channelObject", object);
                     intent.putExtra("firstStart", true);
                     intent.putExtra("sessionId",sessionId);
                     startActivity(intent);
                  }else {
                     LiveTvUtil.showAlert(getActivity(),"Video adres hatası",
                        "Hata keydedildi. En kısa sürede çözülecektir.");
                  }

               }else {
                  LiveTvUtil.showAlert(getActivity(),"Video adres hatası",
                     "Hata keydedildi. En kısa sürede çözülecektir.");
               }
            }
            return false;
         }
      };
   }


   private View.OnClickListener createChannelClickListener(final ChannelItem.ChannelActionType type, final ChannelResponse object) {

      return new View.OnClickListener() {

         @Override
         public void onClick(View v) {

            Log.d(TAG, "action type : " + type);

            switch (type) {

               case CHANGE_CHANNEL:

                  if (Connectivity.isConnectedWifi(getActivity())) {

                     if (Connectivity.isConnectedFast(getActivity())) {

                     } else {

                     }
                  } else if (Connectivity.isConnectedMobile(getActivity())) {

                  }

                  if (ObjectUtil.isNotNull(object.getUrlList()) && object.getUrlList().size() > 0) {

                     Intent intent = new Intent(getActivity(), VitamioCustomPlayer.class);

                     intent.putExtra("channelObject", object);
                     intent.putExtra("firstStart", true);
                     intent.putExtra("sessionId",sessionId);
                     startActivity(intent);
                  }else {
                      LiveTvUtil.showAlert(getActivity(),"Video adres hatası",
                         "Hata keydedildi. En kısa sürede çözülecektir.");
                  }
                  break;

               case CLOSE:
                  break;


            }
         }
      };


   }


}

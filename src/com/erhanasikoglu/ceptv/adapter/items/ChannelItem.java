package com.erhanasikoglu.ceptv.adapter.items;

import android.view.View;
import com.erhanasikoglu.ceptv.bean.channel.ChannelResponse;

public class ChannelItem {

   public enum ChannelItemType {
      MENU_TITLE, MENU_CHANNEL, MENU_FAVORITE
   }

   public enum ChannelActionType {
      CHANGE_CHANNEL,
      CLOSE

   }

   private ChannelItemType type;
   private View.OnClickListener onClickListener;
   private ChannelResponse channelResponse;
   private View.OnTouchListener onTouchListener;


   public ChannelItem(ChannelItemType type, ChannelResponse channelResponse) {
      this.type = type;
      this.channelResponse = channelResponse;
   }


   public ChannelItemType getType() {
      return type;
   }


   public View.OnClickListener getOnClickListener() {
      return onClickListener;
   }

   public void setOnClickListener(View.OnClickListener onClickListener) {

      this.onClickListener = onClickListener;
   }

   public ChannelResponse getChannelResponse() {
      return channelResponse;
   }

   public void setChannelResponse(ChannelResponse channelResponse) {
      this.channelResponse = channelResponse;
   }

   public View.OnTouchListener getOnTouchListener() {
      return onTouchListener;
   }

   public void setOnTouchListener(View.OnTouchListener onTouchListener) {
      this.onTouchListener = onTouchListener;
   }
}

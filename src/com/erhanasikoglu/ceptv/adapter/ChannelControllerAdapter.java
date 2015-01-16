package com.erhanasikoglu.ceptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.adapter.items.ChannelItem;
import com.erhanasikoglu.ceptv.util.LiveTvConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class ChannelControllerAdapter extends ArrayAdapter<ChannelItem>  {

   private static final String TAG = ChannelControllerAdapter.class.getName();

   private List<ChannelItem> menuList;


   private LayoutInflater mInflater;


   public String selectedChannelId;

   public Context mContext;

   public ChannelControllerAdapter(List<ChannelItem> menuList, Context context,String selectedChannelId) {
      super(context, R.layout.channel_list_item,menuList);
      mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.menuList = menuList;
       this.selectedChannelId = selectedChannelId;
      this.mContext = context;
   }


   @Override
   public int getItemViewType(int position) {
      ChannelItem menuItem = (ChannelItem) menuList.get(position);
      return menuItem.getType().ordinal();
   }

   @Override
   public int getViewTypeCount() {
      return 3;
   }

   @Override
   public int getCount() {
      return menuList.size();
   }

   @Override
   public ChannelItem getItem(int position) {
      return menuList.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {


      final ChannelItem menuItem = (ChannelItem) menuList.get(position);

      switch(menuItem.getType()) {
         case MENU_CHANNEL:
            convertView = mInflater.inflate(R.layout.channel_list_item, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.itemText);
            textView.setText(menuItem.getChannelResponse().getName());


            ImageView logo = (ImageView) convertView.findViewById(R.id.itemImage);
            String imagePath = LiveTvConstants.STATIC_LOGO_URL+menuItem.getChannelResponse().getLogoPath() +".png";
            ImageLoader.getInstance().displayImage(imagePath, logo);


            if(selectedChannelId != null && selectedChannelId.equals(menuItem.getChannelResponse().getId())) {
               convertView.setBackgroundResource(R.drawable.menulabelchoosen);
            }
            convertView.setOnClickListener(menuItem.getOnClickListener());

            break;

         case MENU_FAVORITE:

            break;

      }

      return convertView;
   }




}

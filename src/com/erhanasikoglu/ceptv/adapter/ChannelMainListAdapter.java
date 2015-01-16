package com.erhanasikoglu.ceptv.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.adapter.items.ChannelItem;
import com.erhanasikoglu.ceptv.bean.channel.ProgramResponse;
import com.erhanasikoglu.ceptv.util.LiveTvConstants;
import com.erhanasikoglu.ceptv.util.ObjectUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ChannelMainListAdapter extends ArrayAdapter<ChannelItem> implements Filterable {

   private final LayoutInflater mInflater;
   private Context context;
   private List<ChannelItem> channelList;


   public ChannelMainListAdapter(List<ChannelItem> channelList, Context context) {
      super(context, R.layout.channel_item, channelList);
      mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      this.channelList = channelList;
      this.context = context;
   }


   @Override
   public View getView(int position, View convertView, ViewGroup parent) {


      ChannelItem menuItem = channelList.get(position);


      convertView = mInflater.inflate(R.layout.channel_item, parent, false);

      final ImageView logo = (ImageView) convertView.findViewById(R.id.logo);
      String imagePath = LiveTvConstants.STATIC_LOGO_URL + menuItem.getChannelResponse().getLogoPath() + ".png";
      ImageLoader.getInstance().displayImage(imagePath, logo, new SimpleImageLoadingListener(){
         @Override
         public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            logo.setImageResource(R.drawable.ceptvlogo128);
         }
      });

      TextView channelName = (TextView) convertView.findViewById(R.id.name);
      channelName.setText(menuItem.getChannelResponse().getName());

      ProgramResponse currentProgram = null;
      if (ObjectUtil.isNotNull(menuItem.getChannelResponse().getPrograms())) {
         List<ProgramResponse> programs = menuItem.getChannelResponse().getPrograms();
         Date now = new Date();
         int index = 0;
         for (ProgramResponse program : programs) {

            if (programs.size() > index + 1) {
               try {
                  if (program.getInDateStart().before(now) && programs.get(index + 1).getInDateStart().after(now)) {
                     currentProgram = program;
                     program.setCurrent(true);
                  }
                  program.setEndDate(programs.get(index+1).getStartDate());
                  program.seteDate(programs.get(index+1).getsDate());
               } catch (ParseException e) {
                  e.printStackTrace();
               }
            }
            index++;
         }

      }
      if (ObjectUtil.isNotNull(currentProgram)) {
         TextView programName = (TextView) convertView.findViewById(R.id.programName);
         programName.setText(currentProgram.getName());
         TextView timeInterval = (TextView) convertView.findViewById(R.id.timeInterval);

         String startTime = currentProgram.getStartDate();
         String endTime  = currentProgram.getEndDate();

         StringBuilder sb = new StringBuilder();
         sb.append(startTime);
         if(ObjectUtil.isNotNull(endTime)){
            sb.append("-").append(endTime);
         }
         timeInterval.setText(sb.toString());

      }


      convertView.setOnTouchListener(menuItem.getOnTouchListener());

      return convertView;
   }




   @Override
   public void unregisterDataSetObserver(DataSetObserver observer) {
      if (observer != null) {
         super.unregisterDataSetObserver(observer);
      }
   }



}
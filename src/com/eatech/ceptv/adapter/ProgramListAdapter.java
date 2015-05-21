package com.eatech.ceptv.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.eatech.ceptv.adapter.items.ProgramItem;
import com.eatech.ceptv.R;
import com.eatech.ceptv.util.ObjectUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class ProgramListAdapter extends ArrayAdapter<ProgramItem>  {

   private static final String TAG = ProgramListAdapter.class.getName();

   private Integer selectedItemPosition;


   private List<ProgramItem> menuList;

   private LayoutInflater mInflater;

   public Context mContext;

   public ProgramListAdapter(List<ProgramItem> menuList, Context context) {
      super(context, R.layout.program_list_item, menuList);
      mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.menuList = menuList;
      this.mContext = context;
   }

   @Override
   public ProgramItem getItem(int position) {
      return menuList.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {

      final ProgramItem menuItem = menuList.get(position);


      convertView = mInflater.inflate(R.layout.program_list_item, parent, false);


      TextView programName = (TextView) convertView.findViewById(R.id.programName);
      programName.setText(menuItem.getObject().getName());

      TextView timeInterval = (TextView) convertView.findViewById(R.id.timeInterval);


      String startTime = menuItem.getObject().getStartDate();
      String endTime = menuItem.getObject().getEndDate();

      Date now = new Date();
      try {
         if (menuItem.getObject().isCurrent()) {
            convertView.setBackgroundResource(R.drawable.menulabelchoosen);
            selectedItemPosition = position;
          }else if((menuItem.getObject().getInDateEnd()!=null && menuItem.getObject().geteDate()!= "")
            && menuItem.getObject().getInDateStart().before(now) && menuItem.getObject().getInDateEnd().after(now)){
            convertView.setBackgroundResource(R.drawable.menulabelchoosen);
            selectedItemPosition = position;
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }




      StringBuilder sb = new StringBuilder();
      sb.append(startTime);
      if(ObjectUtil.isNotNull(endTime)){
         sb.append("-").append(endTime);
      }
      timeInterval.setText(sb.toString());

      return convertView;
   }


   @Override
   public void unregisterDataSetObserver(DataSetObserver observer) {
      if (observer != null) {
         super.unregisterDataSetObserver(observer);
      }
   }


   public Integer getSelectedItemPosition() {
      return selectedItemPosition;
   }

   public void setSelectedItemPosition(Integer selectedItemPosition) {
      this.selectedItemPosition = selectedItemPosition;
   }
}

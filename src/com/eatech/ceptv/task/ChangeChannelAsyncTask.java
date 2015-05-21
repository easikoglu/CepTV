package com.eatech.ceptv.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.eatech.ceptv.R;
import com.eatech.ceptv.activity.VitamioCustomPlayer;
import com.eatech.ceptv.bean.channel.ChannelResponse;
import com.eatech.ceptv.surfaceview.ActionViewArea;
import com.eatech.ceptv.util.ObjectUtil;

public class ChangeChannelAsyncTask extends AsyncTask<Void, Void, Boolean> {


   protected final ProgressDialog progress;
   private Context mContext;
   private ChannelResponse object;

   private ActionViewArea actionViewArea;




   public ChangeChannelAsyncTask(Context context, ChannelResponse object, ActionViewArea actionViewArea) {
      this.mContext = context;
      this.progress = new ProgressDialog(context);
      this.progress.setCancelable(false);
      this.progress.setCanceledOnTouchOutside(false);
      this.progress.setMessage(context.getString(R.string.channel_in_progress));
      this.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      this.object = object;
      this.actionViewArea = actionViewArea;
   }


   @Override
   protected void onPostExecute(Boolean value) {

      super.onPostExecute(value);

      this.progress.dismiss();

      if(ObjectUtil.isNotNull(actionViewArea)){

         actionViewArea.setChannelObject(object);
         actionViewArea.refreshProgramList();
         actionViewArea.closePopUpViews();

      }
   }

   @Override
   protected void onPreExecute() {
      super.onPreExecute();
      this.progress.show();
   }


   @Override
   protected Boolean doInBackground(Void... params) {

      ((VitamioCustomPlayer) mContext).changeSource(object);

      return false;

   }


}

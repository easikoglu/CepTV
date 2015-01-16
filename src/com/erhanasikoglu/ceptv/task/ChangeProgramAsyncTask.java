package com.erhanasikoglu.ceptv.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.activity.VitamioCustomPlayer;
import com.erhanasikoglu.ceptv.bean.channel.ChannelResponse;

public class ChangeProgramAsyncTask extends AsyncTask<Void, Void, Boolean> {
 


   RestTaskCallback callback;

   ChannelResponse object;
   
   public ChangeProgramAsyncTask(ChannelResponse object,RestTaskCallback callback) {

      this.callback = callback;
      this.object = object;
   }


   @Override
   protected void onPostExecute(Boolean value) {

      super.onPostExecute(value); 
   }

   @Override
   protected void onPreExecute() {
      super.onPreExecute(); 

   }


   @Override
   protected Boolean doInBackground(Void... params) {

       

      return false;

   }


}

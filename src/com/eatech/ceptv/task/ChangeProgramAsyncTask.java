package com.eatech.ceptv.task;

import android.os.AsyncTask;
import com.eatech.ceptv.bean.channel.ChannelResponse;

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

package com.eatech.ceptv.task;

import android.content.Context;
import android.os.AsyncTask;
import com.eatech.ceptv.bean.StatRequest;
import com.eatech.ceptv.application.api.ApiApplication;

public class LogStatAsyncTask extends AsyncTask<Void, Void, Boolean> {



   private Context mContext;
   private StatRequest statRequest;
   final ApiApplication apiApplication = ApiApplication.getInstance();




   public LogStatAsyncTask(Context context, StatRequest request, String sessionId) {
      this.mContext = context;



      apiApplication.setContext(this.mContext);
     apiApplication.setSessionId(sessionId);
      this.statRequest = request;
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

      apiApplication.statResource(statRequest);
      return false;

   }


}

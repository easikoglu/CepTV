package com.erhanasikoglu.ceptv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.adapter.MenuAdapter;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends FragmentActivity {

   private LoginButton facebookLoginBtn;
   private Button continueBtn;

   static final String TAG = "LoginActivity";


   private UiLifecycleHelper uiHelper;


   private void onSessionStateChange(Session session, SessionState state, Exception exception) {
      if (state.isOpened()) {
         Log.i(TAG, "Logged in...");

         this.startProgram();


      } else if (state.isClosed()) {
         Log.i(TAG, "Logged out...");
      }
   }

   private Session.StatusCallback callback = new Session.StatusCallback() {
      @Override
      public void call(Session session, SessionState state, Exception exception) {
         onSessionStateChange(session, state, exception);
      }
   };


   @Override
   protected void onCreate(Bundle savedInstanceState) {


      super.onCreate(savedInstanceState);


         setContentView(R.layout.login);


         facebookLoginBtn = (LoginButton) findViewById(R.id.facebookLoginBtn);
         facebookLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
     /* facebookLoginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
         @Override
         public void onUserInfoFetched(GraphUser user) {

            if(user!=null && Session.getActiveSession()!=null && Session.getActiveSession().isOpened()) {
               startProgram();
            }
         }
      });*/

         continueBtn = (Button) findViewById(R.id.continueBtn);
         continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startProgram();
            }
         });


      uiHelper = new UiLifecycleHelper(getActivity(), callback);
      uiHelper.onCreate(savedInstanceState);



   }


   @Override
   public void onResume() {
      super.onResume();

      // For scenarios where the main activity is launched and user
      // session is not null, the session state change notification
      // may not be triggered. Trigger it if it's open/closed.
      Session session = Session.getActiveSession();
      if (session != null &&
         (session.isOpened() || session.isClosed())) {
         onSessionStateChange(session, session.getState(), null);
      }
      uiHelper.onResume();
   }


   @Override
   public void onPause() {
      super.onPause();
      uiHelper.onPause();
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      uiHelper.onDestroy();
   }

   @Override
   public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      uiHelper.onSaveInstanceState(outState);
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      uiHelper.onActivityResult(requestCode, resultCode, data);

   }


   private void startProgram() {

      Intent i = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(i);
      finish();
   }


   protected Activity getActivity() {
      return this;
   }
}
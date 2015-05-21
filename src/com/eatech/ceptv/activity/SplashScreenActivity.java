package com.eatech.ceptv.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.eatech.ceptv.R;
import com.facebook.Session;


/**
 * Created by easikoglu on 03.06.2014.
 */
public class SplashScreenActivity extends FragmentActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    private Activity thisActivity;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thisActivity = this;
        setContentView(R.layout.activity_splash_screen);


        final ImageView splashScreen = (ImageView) findViewById(R.id.splashScreen);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fadeout);
                splashScreen.startAnimation(fadeOut);

                Session currentUser = Session.getActiveSession();

                if (currentUser != null && currentUser.isOpened() ) {
                     startApp();
                } else {
                    startLogin();
                }

            }
        }, SPLASH_TIME_OUT);




    }

    private void startApp() {

        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }


    private void startLogin() {

        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }


}

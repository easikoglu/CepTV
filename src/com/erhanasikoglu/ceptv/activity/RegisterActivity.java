package com.erhanasikoglu.ceptv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.widget.RelativeLayout;
import com.erhanasikoglu.ceptv.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


import java.util.Stack;

public class RegisterActivity extends FragmentActivity {

    private static final String TAG = RegisterActivity.class.getName();

    private SlidingPaneLayout slidingPaneLayout;

    private RelativeLayout lyMainView;

    private Stack<Fragment> fragmentStack;

    private Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Parse.initialize(this, "CcFtudDgNo5pzhUnIUJ0OqSCoo41leRCJMfD9aun", "9oLT9zjpLLPu8YHr4z1t7caoZrgVft4hIBwXsRhJ");
        thisActivity = this;
        ParseFacebookUtils.initialize("483827531747624");
        setContentView(R.layout.main);




        ParseUser user = ParseUser.getCurrentUser();

        if (user == null) {


        } else {




        }







    }



}

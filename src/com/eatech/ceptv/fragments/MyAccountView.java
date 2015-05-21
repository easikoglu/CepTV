package com.eatech.ceptv.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.eatech.ceptv.bean.UserRequest;
import com.eatech.ceptv.R;
import com.eatech.ceptv.activity.LoginActivity;
import com.eatech.ceptv.activity.MainActivity;
import com.eatech.ceptv.application.api.ApiApplication;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAccountView extends Fragment {

    private static final String TAG = MyAccountView.class.getName();

    private ProfilePictureView userProfilePictureView;
    private TextView userNameView;
    private TextView userEmail;

    private Button logoutButton;

    private ApiApplication apiApplication = ApiApplication.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.userdetails, container, false);


        userProfilePictureView = (ProfilePictureView) v.findViewById(R.id.userProfilePicture);
        userNameView = (TextView) v.findViewById(R.id.userName);
        userEmail = (TextView) v.findViewById(R.id.userEmail);


        logoutButton = (Button) v.findViewById(R.id.logoutButton);

        if(getActivity() instanceof MainActivity){

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   ((MainActivity) getActivity()).callFacebookLogout();
                }
            };

            logoutButton.setOnClickListener(onClickListener);
        }


        // Fetch Facebook user info if the session is active
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }


        return v;
    }

    private void makeMeRequest(Session session) {
        Request request = Request.newMeRequest( session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {
                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());
                                userProfile.put("email", user.getProperty("email").toString());

                                // Save the user profile info in a user property
                                UserRequest userRequest = new UserRequest();
                                userRequest.setEmail(user.getProperty("email").toString());
                                userRequest.setExternalId(user.getId());
                                userRequest.setName(user.getName());
                                userRequest.setExternalJson(userProfile.toString());

                                apiApplication.userResource(userRequest);

                                // Show the user info
                                updateViewsWithProfileInfo(userProfile);
                            } catch (JSONException e) {
                                Log.d(TAG,
                                        "Error parsing returned user data.");
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d(TAG,
                                        "The facebook session was invalidated.");
                                onLogoutButtonClicked();
                            } else {
                                Log.d(TAG,
                                        "Some other error: "
                                                + response.getError()
                                                .getErrorMessage());
                            }
                        }
                    }
                });
        request.executeAsync();

    }

    private void updateViewsWithProfileInfo(JSONObject userProfile) {
         if (userProfile != null) {
            try {
                if (userProfile.getString("facebookId") != null) {
                    String facebookId = userProfile.get("facebookId")
                            .toString();
                    userProfilePictureView.setProfileId(facebookId);
                } else {
                    // Show the default, blank user profile picture
                    userProfilePictureView.setProfileId(null);
                }
                if (userProfile.getString("name") != null) {
                    userNameView.setText(userProfile.getString("name"));
                } else {
                    userNameView.setText("");
                }
                if (userProfile.getString("email") != null) {
                    userEmail.setText(userProfile.getString("email"));
                } else {
                    userEmail.setText("");
                }


            } catch (JSONException e) {
                Log.d(TAG,
                        "Error parsing saved user data.");
            }

        }
    }

    private void onLogoutButtonClicked() {
          startLoginActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

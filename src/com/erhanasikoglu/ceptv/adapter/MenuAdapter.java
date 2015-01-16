package com.erhanasikoglu.ceptv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.adapter.items.MenuItem;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MenuAdapter extends BaseAdapter {

   private static final String TAG = MenuAdapter.class.getName();

   private ArrayList<MenuItem> menuList;
   private LayoutInflater mInflater;

   public MenuAdapter(ArrayList<MenuItem> menuList, Context context) {
      mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.menuList = menuList;
   }


   @Override
   public int getItemViewType(int position) {
      MenuItem menuItem = (MenuItem) menuList.get(position);
      return menuItem.getType().ordinal();
   }

   @Override
   public int getViewTypeCount() {
      return 3;
   }

   @Override
   public int getCount() {
      return menuList.size();
   }

   @Override
   public Object getItem(int position) {
      return menuList.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(int position, View convertView, final ViewGroup parent) {
      ProfileViewHolder pvHolder = null;
      TitleViewHolder tHolder;
      MenuViewHolder mvHolder;
      FaceViewHolder faceViewHolder;

      final MenuItem menuItem = (MenuItem) menuList.get(position);

      switch (menuItem.getType()) {
         case MENU_PROFILE:
            if (convertView == null) {
               pvHolder = new ProfileViewHolder();
               convertView = mInflater.inflate(R.layout.menu_profile_item, parent, false);
               pvHolder.userNameView = (TextView) convertView.findViewById(R.id.userName);
               pvHolder.logoutButton = (Button) convertView.findViewById(R.id.logoutButton);
               pvHolder.logoutButton.setOnClickListener(menuItem.getOnClickListener());
               pvHolder.userProfilePictureView = (ProfilePictureView) convertView.findViewById(R.id.userProfilePicture);
               Session session = Session.getActiveSession();

               if (session != null && session.isOpened()) {
                  makeMeRequest(pvHolder, session);
               }

               convertView.setTag(pvHolder);
            } else {
               pvHolder = (ProfileViewHolder) convertView.getTag();
            }
            break;
         case MENU_DEFAULT:
            if (convertView == null) {
               mvHolder = new MenuViewHolder();
               convertView = mInflater.inflate(R.layout.menu_default_item, parent, false);
               mvHolder.ivMenuItemIcon = (ImageView) convertView.findViewById(R.id.ivMenuItemIcon);
               mvHolder.tvMenuItemTitle = (TextView) convertView.findViewById(R.id.tvMenuItemTitle);
               convertView.setTag(mvHolder);
            } else {
               mvHolder = (MenuViewHolder) convertView.getTag();
            }
            mvHolder.ivMenuItemIcon.setImageDrawable(menuItem.getMenuIcon());
            mvHolder.tvMenuItemTitle.setText(menuItem.getTitle());

            convertView.setOnClickListener(menuItem.getOnClickListener());
            break;
         case MENU_TITLE_BAR:
            if (convertView == null) {
               tHolder = new TitleViewHolder();
               convertView = mInflater.inflate(R.layout.menu_title_item, parent, false);
               tHolder.tvMenuHeaderTitle = (TextView) convertView.findViewById(R.id.tvMenuHeaderTitle);
               convertView.setTag(tHolder);
            } else {
               tHolder = (TitleViewHolder) convertView.getTag();
            }
            tHolder.tvMenuHeaderTitle.setText(menuItem.getTitle());
            break;
         case MENU_FACEBOOK:
            if (convertView == null) {
               faceViewHolder = new FaceViewHolder();
               convertView = mInflater.inflate(R.layout.menu_facebook_item, parent, false);

               LoginButton authButton = (LoginButton) convertView.findViewById(R.id.authButton);
               authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
               authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
                  @Override
                  public void onUserInfoFetched(GraphUser user) {

                     if (user != null) {
                        Log.i(TAG, user.getName() + " logged in...");
                     }
                  }
               });

               convertView.setTag(faceViewHolder);
            } else {
               faceViewHolder = (FaceViewHolder) convertView.getTag();
            }
            break;

         case MENU_LOGOUT:
            break;
      }

      return convertView;
   }


   public static class ProfileViewHolder {
      private ProfilePictureView userProfilePictureView;
      private TextView userNameView;
      private Button logoutButton;
   }

   public static class TitleViewHolder {
      public TextView tvMenuHeaderTitle;
   }

   public static class MenuViewHolder {
      public ImageView ivMenuItemIcon;
      public TextView tvMenuItemTitle;
   }

   public static class FaceViewHolder {

      public Button faceBookBtn;
      Button faceBookLogoutBtn;
   }


   private void makeMeRequest(final ProfileViewHolder pvHolder, Session session) {
      Request request = Request.newMeRequest(session,
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
                     userProfile.put("email", user.getProperty("email"));

                     // Save the user profile info in a user property
                   /*  ParseUser currentUser = ParseUser
                        .getCurrentUser();
                     currentUser.put("profile", userProfile);
                     currentUser.saveInBackground();*/

                     // Show the user info
                     updateFacebookView(pvHolder, userProfile);
                  } catch (JSONException e) {
                     Log.d(TAG,
                        "Error parsing returned user data.");
                  }

               } else if (response.getError() != null) {
                  if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                     || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                     Log.d(TAG,
                        "The facebook session was invalidated.");
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

   private void updateFacebookView(ProfileViewHolder pvHolder, JSONObject userProfile) {


      if (userProfile == null) {
         return;
      }
      try {
         if (userProfile.getString("facebookId") != null) {
            String facebookId = userProfile.get("facebookId")
               .toString();
            pvHolder.userProfilePictureView.setProfileId(facebookId);
         } else {
            // Show the default, blank user profile picture
            pvHolder.userProfilePictureView.setProfileId(null);
         }
         if (userProfile.getString("name") != null) {
            pvHolder.userNameView.setText(userProfile.getString("name"));
         } else {
            pvHolder.userNameView.setText("");
         }


      } catch (JSONException e) {
         Log.d(TAG,
            "Error parsing saved user data.");
      }
   }


}

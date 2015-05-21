package com.eatech.ceptv.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.eatech.ceptv.adapter.items.MenuItem;
import com.eatech.ceptv.enums.MenuItemType;
import com.eatech.ceptv.fragments.HomeView;
import com.eatech.ceptv.fragments.MyAccountView;
import com.eatech.ceptv.util.LiveTvConstants;
import com.eatech.ceptv.R;
import com.eatech.ceptv.adapter.MenuAdapter;
import com.eatech.ceptv.enums.MenuActionType;
import com.eatech.ceptv.util.ObjectUtil;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import io.vov.vitamio.LibsChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

   private static final String TAG = MainActivity.class.getName();

   private MySlidingPaneLayout slidingPaneLayout;

   private RelativeLayout lyMainView;

   private Stack<Fragment> fragmentStack;

   private Map<MenuActionType, Fragment> menuFragment;


   private Button btnBulletinTopMenu;

   private ListView lvMainMenu;

   private MenuAdapter menuAdapter;


   private RelativeLayout topMenuBar;
   private String sessionId;

   SharedPreferences prefs;


   private UiLifecycleHelper uiHelper;


   private void onSessionStateChange(Session session, SessionState state, Exception exception) {
      if (state.isOpened()) {
         Log.i(TAG, "Logged in...");

         runOnUiThread(new Runnable() {
            @Override
            public void run() {
               lvMainMenu.refreshDrawableState();
               menuAdapter = new MenuAdapter(createMenuItemList(), getActivity());
               menuAdapter.notifyDataSetChanged();
               lvMainMenu.setAdapter(menuAdapter);


            }
         });


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

      setContentView(R.layout.main);

      fragmentStack = new Stack<Fragment>();

      menuFragment = new HashMap<MenuActionType, Fragment>();
      slidingPaneLayout = (MySlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
      slidingPaneLayout.setSliderFadeColor(0);

      lyMainView = (RelativeLayout) findViewById(R.id.lyMainView);
      lvMainMenu = (ListView) findViewById(R.id.lvMainMenu);


      lvMainMenu.setAdapter(createMenuAdapter());

      topMenuBar = (RelativeLayout) findViewById(R.id.topBar);
      btnBulletinTopMenu = (Button) findViewById(R.id.btnBulletinTopMenu);
      btnBulletinTopMenu.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            toggleMenu();
         }
      });


      uiHelper = new UiLifecycleHelper(getActivity(), callback);
      uiHelper.onCreate(savedInstanceState);


      startHomeView();

      if (!LibsChecker.checkVitamioLibs(getActivity()))
         return;
   }


   public MenuAdapter createMenuAdapter() {
      ArrayList<MenuItem> menuList = createMenuItemList();

      menuAdapter = new MenuAdapter(menuList, this);
      return menuAdapter;

   }




   public void toggleMenu() {
      if (slidingPaneLayout.isOpen()) {
         slidingPaneLayout.closePane();
      } else {
         slidingPaneLayout.openPane();
      }
   }


   public boolean removeFromFragmentStack() {
      if (fragmentStack.size() > 1) {
         Fragment fragment = fragmentStack.pop();
         Fragment fold = fragmentStack.get(fragmentStack.size() - 1);
         fold.onResume();

         FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
         tx.remove(fragment);
         tx.commit();

         return true;
      } else return false;
   }


   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      uiHelper.onActivityResult(requestCode, resultCode, data);

   }

	/*MENU ILE ILGILI KISIM*/

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      // TODO Auto-generated method stub
      Log.e(TAG, "menuItemClicked");

   }

   public ArrayList<MenuItem> createMenuItemList() {
      ArrayList<MenuItem> menuList = new ArrayList<MenuItem>();

      Session session = Session.getActiveSession();
      if (session != null && session.isOpened()) {
         //profileItem with images and welcome message
         MenuItem profileItem = new MenuItem(MenuItemType.MENU_PROFILE);
         profileItem.setOnClickListener(createClickListener(MenuActionType.FACE_LOGOUT));
         //title-bar for profile
         MenuItem titleProfilItem = new MenuItem(MenuItemType.MENU_TITLE_BAR, "Profil");//Profil
         //item for profile
         MenuItem myAccountItem = new MenuItem(MenuActionType.MY_ACCOUNT,
            MenuItemType.MENU_DEFAULT,
            "Hesabım", getResources().getDrawable(R.drawable.home32));//Hesabım
         myAccountItem.setOnClickListener(createClickListener(MenuActionType.MY_ACCOUNT));


         menuList.add(profileItem);
         menuList.add(titleProfilItem);
         menuList.add(myAccountItem);
      } else {
         MenuItem menuItem = new MenuItem(MenuItemType.MENU_FACEBOOK, MenuActionType.FACE_LOGIN);
         menuItem.setOnClickListener(createClickListener(MenuActionType.FACE_LOGIN));

         menuList.add(menuItem);
      }
      MenuItem channelAppItem = new MenuItem(MenuActionType.CHANNEL_LIST,
         MenuItemType.MENU_DEFAULT,
         "Kanallar", getResources().getDrawable(R.drawable.tv32));//Channels
      channelAppItem.setOnClickListener(createClickListener(MenuActionType.CHANNEL_LIST));

      menuList.add(channelAppItem);

      MenuItem closeAppItem = new MenuItem(MenuActionType.CLOSE,
         MenuItemType.MENU_DEFAULT,
         "Çıkış", getResources().getDrawable(R.drawable.minus32));//logout
      closeAppItem.setOnClickListener(createClickListener(MenuActionType.CLOSE));

      menuList.add(closeAppItem);


      return menuList;

   }

   private boolean checkFromMap(MenuActionType type) {
      if (!menuFragment.isEmpty()) {
         Fragment fragment = menuFragment.get(type);
         if (ObjectUtil.isNotNull(fragment)) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.main, fragment);
            tx.commit();
            return true;
         } else {
            return false;
         }

      }
      return false;
   }

   private View.OnClickListener createClickListener(final MenuActionType type) {

      return new View.OnClickListener() {

         @Override
         public void onClick(View v) {


            switch (type) {

               case MY_ACCOUNT:
                  if (!checkFromMap(type)) {
                     MyAccountView myAccountView = new MyAccountView();
                     fragmentStack.add(myAccountView);
                     menuFragment.put(type, myAccountView);
                     FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                     tx.replace(R.id.main, myAccountView);
                     tx.commit();
                  }
                  toggleMenu();
                  break;
               case CLOSE:
                  closeApplication();

                  break;

               case CHANNEL_LIST:

                  if (!checkFromMap(type)) {
                     runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           lvMainMenu.refreshDrawableState();
                           menuAdapter = new MenuAdapter(createMenuItemList(), getActivity());
                           menuAdapter.notifyDataSetChanged();
                           lvMainMenu.setAdapter(menuAdapter);

                           HomeView bulletinView = new HomeView();
                           slidingPaneLayout.setShouldSwipe(true);
                           fragmentStack.add(bulletinView);

                           menuFragment.put(type, bulletinView);

                           FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                           tx.replace(R.id.main, bulletinView);
                           tx.commit();
                        }
                     });
                  }
                  toggleMenu();

                  break;
               case FACE_LOGIN:
                /*  List<String> permissions = Arrays.asList("email", "public_profile",
                     "user_friends");
                  ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
                     @Override
                     public void done(ParseUser user, ParseException err) {

                        if (user == null) {
                           Log.d(TAG,
                              "Uh oh. The user cancelled the Facebook login.");
                        } else {


                           if (user.isNew()) {
                              Log.d(TAG,
                                 "User signed up and logged in through Facebook!");


                           } else {
                              Log.d(TAG,
                                 "User logged in through Facebook!");
                           }


                           linkParseUserToFacebook(user);

                           runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                 startUSerDetailsFragment();
                                 lvMainMenu.refreshDrawableState();
                                 menuAdapter = new MenuAdapter(createMenuItemList(), getActivity());
                                 menuAdapter.notifyDataSetChanged();
                                 lvMainMenu.setAdapter(menuAdapter);


                              }
                           });

                        }

                     }
                  });*/
                  break;
               case FACE_LOGOUT:
                  callFacebookLogout();
                  toggleMenu();

                  break;
            }

         }
      };
   }

   public void callFacebookLogout() {
      Session session = Session.getActiveSession();
      if (session != null) {

         if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
         }
      } else {
         session = new Session(this.getApplicationContext());
         Session.setActiveSession(session);

         session.closeAndClearTokenInformation();

      }
      runOnUiThread(new Runnable() {
         @Override
         public void run() {
            lvMainMenu.refreshDrawableState();
            menuAdapter = new MenuAdapter(createMenuItemList(), getActivity());
            menuAdapter.notifyDataSetChanged();
            lvMainMenu.setAdapter(menuAdapter);
            startHomeView();
         }
      });

   }


   private void closeApplication() {
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
      alertDialogBuilder.setTitle("Uygulamadan Çıkış");
      alertDialogBuilder
         .setMessage("Uygulamayı kapat?")
         .setCancelable(false)
         .setPositiveButton("Evet",
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                  getActivity().finish();
                  System.exit(0);
               }
            })

         .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

               dialog.cancel();
            }
         });

      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();
   }

   public void startHomeView() {
      HomeView bulletinView = new HomeView();
      slidingPaneLayout.setShouldSwipe(true);
      fragmentStack.add(bulletinView);
      FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
      tx.replace(R.id.main, bulletinView);
      tx.commit();

   }

   private void startUSerDetailsFragment() {

      MyAccountView myAccountView = new MyAccountView();
      fragmentStack.add(myAccountView);
      FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
      tx.replace(R.id.main, myAccountView);
      tx.commit();
   }

   @Override
   public void onBackPressed() {
      if (fragmentStack.size() > 1) {
         Fragment fragment = fragmentStack.pop();
         Fragment fold = fragmentStack.get(fragmentStack.size() - 1);
         fold.onResume();

         FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
         tx.remove(fragment);
         tx.commit();
         closeApplication();
         return;
      }
      super.onBackPressed();
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


   protected Activity getActivity() {
      return this;
   }

   public RelativeLayout getTopMenuBar() {
      return topMenuBar;
   }

   public void setTopMenuBar(RelativeLayout topMenuBar) {
      this.topMenuBar = topMenuBar;
   }


   public String getSessionId() {
      prefs = this.getSharedPreferences(
         "com.erhanasikoglu.ceptv", Context.MODE_PRIVATE);
      return prefs.getString(LiveTvConstants.SESSION_ID, null);
   }

   public String getApplicationId() {
      return getString(R.string.mauth_application_id);
   }

   public String getClientId() {
      return getString(R.string.mauth_client_id);
   }
}

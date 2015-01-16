package com.erhanasikoglu.ceptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.erhanasikoglu.ceptv.R;
import com.erhanasikoglu.ceptv.adapter.items.MenuItem;


import java.util.ArrayList;


public class FaceBookAdapter extends BaseAdapter {

	private static final String TAG = FaceBookAdapter.class.getName();

	private ArrayList<MenuItem> menuList;
	private LayoutInflater mInflater;

	public FaceBookAdapter(ArrayList<MenuItem> menuList, Context context){
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.menuList = menuList;
	}


    @Override
    public int getItemViewType(int position) {
    	MenuItem menuItem = (MenuItem)menuList.get(position);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ProfileViewHolder pvHolder = null;
		TitleViewHolder tHolder = null;
		MenuViewHolder mvHolder = null;

		final MenuItem menuItem = (MenuItem)menuList.get(position);

        switch (menuItem.getType()) {
            case MENU_FACEBOOK:
                if (convertView == null) {
                    pvHolder = new ProfileViewHolder();
                    convertView = mInflater.inflate(R.layout.menu_facebook_item, parent, false);

                    convertView.setTag(pvHolder);
                } else {
                    pvHolder = (ProfileViewHolder) convertView.getTag();
                }
                break;
        }



        return convertView;
	}


    public static class ProfileViewHolder {
        public TextView textView;
    }

    public static class TitleViewHolder {
        public TextView tvMenuHeaderTitle;
    }

    public static class MenuViewHolder {
    	public ImageView ivMenuItemIcon;
        public TextView tvMenuItemTitle;
        public Button btnMakeMeHomePage;
    }

}

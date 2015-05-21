package com.eatech.ceptv.adapter.items;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.eatech.ceptv.enums.MenuItemType;
import com.eatech.ceptv.enums.MenuActionType;

public class MenuItem {
	

	

	
	private MenuItemType type;
	private MenuActionType actionType;
	private String title;
	private Drawable menuIcon;
	private boolean canbeHome;
	private boolean isHomeMenu;
    private View.OnClickListener onClickListener;

	
	public MenuItem(MenuItemType type){
		this.type = type;
		this.isHomeMenu = false;
		this.canbeHome = true;
	}
	
	public MenuItem(MenuItemType type, String title){
		this(type);
		this.title = title;
	}
	
	public MenuItem(MenuActionType actionType, MenuItemType type, String title, Drawable menuIcon){
		this(type,title);
		this.actionType = actionType;
		this.menuIcon = menuIcon;
	}

    public MenuItem(MenuItemType type, MenuActionType actionType) {
        this.type = type;
        this.actionType = actionType;
    }

    public MenuItem(MenuActionType actionType, MenuItemType type, String title, Drawable menuIcon, boolean isHomeMenu, boolean canbeHome){
		this(actionType,type,title,menuIcon);
		this.isHomeMenu = isHomeMenu;
		this.canbeHome = canbeHome;
	}

	public MenuItemType getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public Drawable getMenuIcon() {
		return menuIcon;
	}

	public boolean isHomeMenu() {
		return isHomeMenu;
	}

	public boolean isCanbeHome() {
		return canbeHome;
	}

	public MenuActionType getActionType() {
		return actionType;
	}

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;
    }
}

package com.eatech.ceptv.activity;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MySlidingPaneLayout extends SlidingPaneLayout {


	private boolean shouldSwipe;

	public MySlidingPaneLayout(Context context) {
		super(context);
	}

	public MySlidingPaneLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return shouldSwipe ? super.onInterceptTouchEvent(arg0): shouldSwipe;
	}


	public boolean isShouldSwipe() {
		return shouldSwipe;
	}

	public void setShouldSwipe(boolean shouldSwipe) {
		this.shouldSwipe = shouldSwipe;
	}
}

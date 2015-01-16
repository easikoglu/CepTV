package com.erhanasikoglu.ceptv.adapter.items;

import android.view.View;
import com.erhanasikoglu.ceptv.bean.channel.ProgramResponse;

public class ProgramItem {



	private ProgramResponse object;

	private View.OnClickListener onClickListener;

	public ProgramItem(ProgramResponse object) {
		this.object = object;
	}

	public ProgramItem(ProgramResponse object, View.OnClickListener onClickListener) {
		this.object = object;
		this.onClickListener = onClickListener;
	}

	public View.OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public ProgramResponse getObject() {

		return object;
	}

	public void setObject(ProgramResponse object) {
		this.object = object;
	}
}

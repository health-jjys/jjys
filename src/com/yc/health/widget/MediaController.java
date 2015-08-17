package com.yc.health.widget;

import android.content.Context;
import android.util.AttributeSet;

public class MediaController extends android.widget.MediaController{

	public MediaController(Context context) {
		super(context);
	}
	
	public MediaController(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void hide() {
		super.show();
	}
}

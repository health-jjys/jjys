package com.yc.health;

import org.kymjs.kjframe.KJActivity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

public class GuideActivity extends KJActivity {

	@Override
	public void setRootView() {
		setContentView(R.layout.guide);
	}

	@Override
	public void initData() {
		super.initData();
		
		new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(GuideActivity.this, HomeActivity.class);
                GuideActivity.this.startActivity(mainIntent);
                GuideActivity.this.finish();
            }
        }, 500);
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
	}
}

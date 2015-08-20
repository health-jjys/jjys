package com.yc.health;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

public class GuideActivity extends InstrumentedActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(GuideActivity.this, HomeActivity.class);
                GuideActivity.this.startActivity(mainIntent);
                GuideActivity.this.finish();
            }
        }, 500);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//JPushInterface.onPause(this);
		super.onPause();
	}
}
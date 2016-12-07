package com.example.helloworld;

import com.example.helloworld.fragments.pages.FeedsListFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		textView = (TextView) findViewById(R.id.rom);
		textView.setText(getIntent().getStringExtra("text"));
	}

	

}

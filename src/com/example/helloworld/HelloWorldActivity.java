package com.example.helloworld;

import com.example.helloworld.fragments.MainTabbarFragment;
import com.example.helloworld.fragments.MainTabbarFragment.OnTabSelectedListener;
import com.example.helloworld.fragments.pages.FeedsListFragment;
import com.example.helloworld.fragments.pages.MyProfileFragment;
import com.example.helloworld.fragments.pages.NotesListFragment;
import com.example.helloworld.fragments.pages.SearchListFragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class HelloWorldActivity extends Activity {

	FeedsListFragment contentFeedList = new FeedsListFragment();
	NotesListFragment contentNoteList = new NotesListFragment();
	MyProfileFragment contentMyProfile = new MyProfileFragment();
	SearchListFragment contentSearchList = new SearchListFragment();

	Fragment fragPage[]={contentFeedList,contentNoteList,contentSearchList,contentMyProfile};

	MainTabbarFragment tabbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_helloworld);

		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		tabbar.setSelectedItem(0);
	}
	void changeContentFragment(int index){

		if(index == -1) return;

		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, fragPage[index])
		.commit();
	}



}

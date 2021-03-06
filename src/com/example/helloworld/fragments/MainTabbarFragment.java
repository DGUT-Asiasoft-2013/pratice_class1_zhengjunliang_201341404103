package com.example.helloworld.fragments;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.NewContentActivity;
import com.example.helloworld.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainTabbarFragment extends Fragment {

	View btnNew,tabFeeds,tabNotes,tabSearch,tabMe;
	View[] tabs;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_widget_main_tabbar, null);

		btnNew = view.findViewById(R.id.btn_new);
		tabFeeds = view.findViewById(R.id.tab_feeds);
		tabNotes = view.findViewById(R.id.tab_notes);
		tabSearch = view.findViewById(R.id.tab_search);
		tabMe = view.findViewById(R.id.tab_me);

		tabs = new View[]{
				tabFeeds,tabNotes,tabSearch,tabMe
		};

		for(final View tab : tabs){
			tab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					onTabClicked(tab); 

				}
			});
		}
		
		btnNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goNewContent();
				
			}
		});



		return view;
	}
	public static interface OnTabSelectedListener {
		void onTabSelected(int index);
	}

	OnTabSelectedListener onTabSelectedListener;

	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
		this.onTabSelectedListener = onTabSelectedListener;
	}

	public void setSelectedItem(int index){
		if(index>=0 && index<tabs.length){
			onTabClicked(tabs[index]);
		}
	}

	public void onTabClicked(View tab){
		int  selectedIndex = -1;
		for(int i=0;i<tabs.length;i++){
			View otherTab = tabs[i];
			if(otherTab == tab){
				otherTab.setSelected(true);
				selectedIndex = i;
			}else{
				otherTab.setSelected(false);
			}
		}
		if(onTabSelectedListener!=null && selectedIndex>=0){
			onTabSelectedListener.onTabSelected(selectedIndex);
		}
	}
	
	void goNewContent(){
		Intent itnt = new Intent(getActivity(),NewContentActivity.class);
		startActivity(itnt);
		
		getActivity().overridePendingTransition(0,R.anim.slide_in_bottom);
	}
	
	public int getSelectedIndex(){
 		for(int i=0; i<tabs.length; i++){
 			if(tabs[i].isSelected()) return i;
 		}
 		
 		return -1;
 	}
}

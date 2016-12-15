package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.api.entity.Page;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchPageFragment extends Fragment {
	View view;
	ListView listView;
	AvatarView avatar;
	TextView author;
	TextView title;
	TextView content;
	TextView createDate;
	EditText edit;

	int page;	
	List<Article> data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null){
			view = inflater.inflate(R.layout.fragment_page_search_page, null);
			
			edit = (EditText) view.findViewById(R.id.edit_search);

			listView = (ListView) view.findViewById(R.id.list_search);
			listView.setAdapter(listAdapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});

			view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					search();					
				}
			});
		}
		return view;
	}
	

	protected void search() {
		reload();
	}


	

	@Override
	public void onResume() {
		super.onResume();
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_page_feed_list_item, null);	
			}else{
				view = convertView;
			}

			content = (TextView) view.findViewById(R.id.feed_item_text);
			avatar = (AvatarView) view.findViewById(R.id.feed_item_avatar);
			author = (TextView) view.findViewById(R.id.feed_item_author);
			title = (TextView) view.findViewById(R.id.feed_item_title);
			createDate = (TextView) view.findViewById(R.id.feed_item_createdate);


			Article article = data.get(position);

			content.setText(article.getText());
			avatar.load(Server.serverAddress + article.getAuthor().getAvatar());
			author.setText(article.getAuthor().getName());
			title.setText(article.getTitle());
			createDate.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(article.getCreateDate()));

			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			return data==null ? 0 : data.size();
		}
	};

	void reload(){
		OkHttpClient client = Server.getSharedClient();
		Request request = Server.requestBuilderWithApi("article/s/"+edit.getText())
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					Page<Article> article = new ObjectMapper().readValue(arg1.body().string(), 
							new TypeReference<Page<Article>>() {});
					//							.readValue(arg1.body().bytes(), Article.class);

					SearchPageFragment.this.page = article.getNumber();
					SearchPageFragment.this.data = article.getContent();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							listAdapter.notifyDataSetInvalidated();
						}
					});					
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
						}
					});
				}
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(arg1.getMessage())
						.show();
					}
				});
			}
		});
	}


	void onItemClicked(int position){

		Intent itnt = new Intent(getActivity(), FeedContentActivity.class);

		itnt.putExtra("article", data.get(position));
		startActivity(itnt);
	}
}

package com.example.helloworld.fragments.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.helloworld.FeedContentActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Comment;
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
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoteListFragment extends Fragment {
	View view;
	ListView listView;
	AvatarView avatar;
	TextView author;
	TextView title;
	TextView content;
	TextView createDate;
	TextView lab;

	int page;	
	List<Comment> data;

	View btnLoadMore;
	TextView textLoadMore;
	String tar = "tome/";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null){
			view = inflater.inflate(R.layout.fragment_page_note_list, null);

			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text_more);


			listView = (ListView) view.findViewById(R.id.list_my_comment);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);


			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);
				}
			});

			btnLoadMore.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					loadmore();

				}
			});

		}

		return view;
	}

	void loadmore(){

		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中...");

		Request request = Server.requestBuilderWithApi("comments/"+tar+"/"+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});

				try{
					Page<Comment> feeds = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {});
					if(feeds.getNumber()>page){
						if(data==null){
							data = feeds.getContent();
						}else{
							data.addAll(feeds.getContent());
						}
						page = feeds.getNumber();

						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								listAdapter.notifyDataSetChanged();
							}
						});
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
					}
				});
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		reload();
		view.findViewById(R.id.btn_comment_tome).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				tar = "tome";	
				reload();
			}
		});

		view.findViewById(R.id.btn_comment_my).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				tar = "my";		
				reload();
			}
		});
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_comment_me_list_item, null);	
			}else{
				view = convertView;
			}

			lab = (TextView) view.findViewById(R.id.note_lab);
			content = (TextView) view.findViewById(R.id.tome_item_text);
			avatar = (AvatarView) view.findViewById(R.id.tome_item_avatar);
			author = (TextView) view.findViewById(R.id.tome_item_author);
			title = (TextView) view.findViewById(R.id.tome_item_title);
			createDate = (TextView) view.findViewById(R.id.tome_item_createdate);



			Comment comment = data.get(position);

			if(tar.equals("tome")){
				lab.setText("评论了");
				content.setText(comment.getText());
				avatar.load(Server.serverAddress + comment.getAuthor().getAvatar());
				author.setText(comment.getAuthor().getName());
				title.setText(comment.getArticle().getTitle());
				createDate.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(comment.getCreateDate()));
			}else{
				lab.setText("你评论了");
				content.setText(comment.getText());
				avatar.load(Server.serverAddress + comment.getArticle().getAuthor().getAvatar());
				author.setText(comment.getArticle().getAuthor().getName());
				title.setText(comment.getArticle().getTitle());
				createDate.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(comment.getCreateDate()));
			}


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
		Request request = Server.requestBuilderWithApi("comments/"+tar)
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					Page<Comment> comment = new ObjectMapper().readValue(arg1.body().string(), 
							new TypeReference<Page<Comment>>() {});
					//							.readValue(arg1.body().bytes(), Comment.class);

					NoteListFragment.this.page = comment.getNumber();
					NoteListFragment.this.data = comment.getContent();
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

		if(tar.equals("my")){
			Intent itnt = new Intent(getActivity(), FeedContentActivity.class);

			itnt.putExtra("article", data.get(position).getArticle());
			startActivity(itnt);
		}
	}
}

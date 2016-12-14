package com.example.helloworld.fragments;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;


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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentListFragment extends Fragment {

	View view;
	ListView listView;
	AvatarView avatar;
	TextView author;
	TextView content;
	TextView createDate;

	int page;	
	List<Comment> data;

	View btnLoadMore;
	TextView textLoadMore;

	String articleId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null){
			view = inflater.inflate(R.layout.fragment_comment_list, null);

			btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text_more);

			listView = (ListView) view.findViewById(R.id.list_comment);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);
		}

		return view;
	}

	void loadmore(){

		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中...");

		Request request = Server.requestBuilderWithApi("article/"+getArticleId()+"comments"+(page+1)).get().build();
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
					Page<Comment> comments = new ObjectMapper().readValue(arg1.body().string(), new TypeReference<Page<Comment>>() {});
					if(comments.getNumber()>page){
						if(data==null){
							data = comments.getContent();
						}else{
							data.addAll(comments.getContent());
						}
						page = comments.getNumber();

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
	}

	BaseAdapter listAdapter = new BaseAdapter() {

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if(convertView==null){
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				view = inflater.inflate(R.layout.fragment_comment_list_item, null);	
			}else{
				view = convertView;
			}

			content = (TextView) view.findViewById(R.id.comment_item_text);
			avatar = (AvatarView) view.findViewById(R.id.comment_item_avatar);
			author = (TextView) view.findViewById(R.id.comment_item_author);
			createDate = (TextView) view.findViewById(R.id.comment_item_createdate);



			Comment comment = data.get(position);

			content.setText(comment.getText());
			avatar.load(Server.serverAddress + comment.getAuthor().getAvatar());
			author.setText(comment.getAuthor().getName());
			createDate.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(comment.getCreateDate()));

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
		Request request = Server.requestBuilderWithApi("article/"+getArticleId()+"/comments")
				.build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, Response arg1) throws IOException {
				try {
					Page<Comment> comment = new ObjectMapper().readValue(arg1.body().string(), 
							new TypeReference<Page<Comment>>() {});
					//							.readValue(arg1.body().bytes(), Comment.class);

					CommentListFragment.this.page = comment.getNumber();
					CommentListFragment.this.data = comment.getContent();
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

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}



}

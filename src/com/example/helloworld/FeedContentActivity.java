package com.example.helloworld;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.fragments.CommentListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedContentActivity extends Activity {

	Button btn_like;
	boolean checklike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feed_content);
		Article article = (Article) getIntent().getSerializableExtra("article");

		TextView textView = (TextView) findViewById(R.id.feed_content_text);
		textView.setText(article.getText());

		TextView authorView = (TextView) findViewById(R.id.feed_content_author);
		authorView.setText(article.getAuthor().getName());

		TextView titleView = (TextView) findViewById(R.id.feed_content_title);
		titleView.setText(article.getTitle());

		AvatarView avatarView = (AvatarView) findViewById(R.id.feed_content_avatar);
		avatarView.load(Server.serverAddress + article.getAuthor().getAvatar());

		TextView createdateView = (TextView) findViewById(R.id.feed_content_createdate);
		createdateView.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(article.getCreateDate()));

		btn_like = (Button) findViewById(R.id.btn_like);

		findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				goComment();

			}
		});

		findViewById(R.id.btn_like).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkLikes();
				changeLikes();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkLikes();
		
	}
	
	String likesnum;
	void checkLikes(){
		Article article = (Article) getIntent().getSerializableExtra("article");
		
		Request requestLike = Server.requestBuilderWithApi("article/"+article.getId()+"/likes")
				.build();
		Server.getSharedClient().newCall(requestLike).enqueue(new Callback() {


			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				final String num = arg1.body().string();
				runOnUiThread(new Runnable() {					
					public void run() {
						likesnum = num;
				
					}
				});
							
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});

		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/isliked")
				.get()
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {


			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							if(arg1.body().string().equals("true")){
								checklike = true;
								btn_like.setText("ÒÑÔÞ("+likesnum+")");
							}else{
								checklike = false;
								btn_like.setText("ÔÞ("+likesnum+")");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				});
							
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	void changeLikes() {
		Article article = (Article) getIntent().getSerializableExtra("article");

		MultipartBody body = new MultipartBody
				.Builder()
				.addFormDataPart("likes", String.valueOf(!checklike))
				.build();

		Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/likes")
				.post(body)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						checkLikes();
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
			}
		});

	}

	void goComment() {
		Intent itnt = new Intent(this,CommentActivity.class);
		Intent itnt2 = new Intent(this,CommentListFragment.class);
		itnt.putExtra("article", getIntent().getSerializableExtra("article"));
		itnt2.putExtra("article", getIntent().getSerializableExtra("article"));
		startActivity(itnt);

	}
}

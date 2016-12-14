package com.example.helloworld;

import java.text.SimpleDateFormat;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.fragments.CommentListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_content);
		Article article = (Article) getIntent().getSerializableExtra("article");
		
		TextView textView = (TextView) findViewById(R.id.feed_content_text);
		textView.setText(article.getText());
		
		TextView authorView = (TextView) findViewById(R.id.feed_content_author);
		authorView.setText(article.getAuthorName());
		
		TextView titleView = (TextView) findViewById(R.id.feed_content_title);
		titleView.setText(article.getTitle());
		
		AvatarView avatarView = (AvatarView) findViewById(R.id.feed_content_avatar);
		avatarView.load(Server.serverAddress + article.getAuthorAvatar());
		
		TextView createdateView = (TextView) findViewById(R.id.feed_content_createdate);
		createdateView.setText(new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(article.getCreateDate()));
		
		findViewById(R.id.btn_comment).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goComment();
				
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

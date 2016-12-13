package com.example.helloworld;

import java.text.SimpleDateFormat;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FeedContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feed_content);
		Article article = (Article) getIntent().getSerializableExtra("article");
		String text = article.getText();
		String author = article.getAuthorName();
		String title = article.getTitle();
		String avatar = article.getAuthorAvatar();
		String createdate = new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss").format(article.getCreateDate()); 
		
		TextView textView = (TextView) findViewById(R.id.feed_content_text);
		textView.setText(text);
		
		TextView authorView = (TextView) findViewById(R.id.feed_content_author);
		authorView.setText(author);
		
		TextView titleView = (TextView) findViewById(R.id.feed_content_title);
		titleView.setText(title);
		
		AvatarView avatarView = (AvatarView) findViewById(R.id.feed_content_avatar);
		avatarView.load(Server.serverAddress + avatar);
		
		TextView createdateView = (TextView) findViewById(R.id.feed_content_createdate);
		createdateView.setText(createdate);
		
	}
}

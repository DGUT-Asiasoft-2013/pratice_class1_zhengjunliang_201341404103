package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.Article;
import com.example.helloworld.fragments.CommentListFragment;
import com.example.helloworld.fragments.widgets.AvatarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

public class CommentActivity extends Activity {
	CommentListFragment commentListFrag;
	EditText editComment;

	ListView listView;

	AvatarView avatar;
	TextView author;
	TextView content;
	TextView createDate;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);
		commentListFrag = (CommentListFragment) getFragmentManager().findFragmentById(R.id.comment_list_frag);		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Article article = (Article)getIntent().getSerializableExtra("article");
		commentListFrag.setArticleId(article.getId().toString());

		editComment = (EditText) findViewById(R.id.comment_edit);
		findViewById(R.id.btn_send_comment).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				sendComment();				
			}
		});
	}

	void sendComment() {
		String comment = editComment.getText().toString();
		Article article = (Article)getIntent().getSerializableExtra("article");
		MultipartBody body = new MultipartBody.Builder()
				.addFormDataPart("article_id",article.getId().toString())
				.addFormDataPart("text", comment)
				.build();

		Request request = Server.requestBuilderWithApi("article/"+article.getId().toString()+"/comments")
				.post(body)
				.build();

		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				final String responseBody = arg1.body().string();

				runOnUiThread(new Runnable() {
					public void run() {
						CommentActivity.this.onSucceed(responseBody);
					}
				});
			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						CommentActivity.this.onFailure(arg1);
					}
				});
			}
		});
	}

	void onSucceed(String text){
		new AlertDialog.Builder(this).setMessage(text)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
			}
		}).show();
	}

	void onFailure(Exception e){
		new AlertDialog.Builder(this).setMessage(e.getMessage()).show();
	}

}

package com.example.helloworld.fragments;

import java.io.IOException;

import com.example.helloworld.HelloWorldActivity;
import com.example.helloworld.LoginActivity;
import com.example.helloworld.MD5;
import com.example.helloworld.PasswordRecoverActivity;
import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.User;
import com.example.helloworld.fragments.PasswordChangeFragment.OnSubmitListener;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.helloworld.fragments.pages.MyProfileFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordChangeActivity extends Activity {
	SimpleTextInputCellFragment fragPassword;
	SimpleTextInputCellFragment fragPasswordRepeat;
	SimpleTextInputCellFragment fragOldPassword;
	PasswordChangeFragment fragPasswordChange = new PasswordChangeFragment();
	String oldPassword;
	String password;
	String passwordRepeat;
	String account;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_password_recover);

		fragPasswordChange.setOnSubmitListener(new OnSubmitListener() {

			@Override
			public void onSubmit() {
				changePassword();
			}
		});


		getFragmentManager().beginTransaction().replace(R.id.container, fragPasswordChange).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	void changePassword(){
		oldPassword = fragPasswordChange.getOldPassword();
		password = fragPasswordChange.getPassword();
		passwordRepeat = fragPasswordChange.getPasswordRepeat();

		if(!password.equals(passwordRepeat)){

			new AlertDialog.Builder(PasswordChangeActivity.this)
			.setMessage("新密码不一致")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setNegativeButton("好", null)
			.show();

			return;
		}

		password = MD5.getMD5(password);
		oldPassword = MD5.getMD5(oldPassword);

		OkHttpClient client = Server.getSharedClient();

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("passwordHash", oldPassword)
				.addFormDataPart("newPasswordHash", password);



		Request request = Server.requestBuilderWithApi("passwordchange")
				.method("post", null)
				.post(requestBodyBuilder.build())
				.build();

		final ProgressDialog progressDialog = new ProgressDialog(PasswordChangeActivity.this);
		progressDialog.setMessage("请稍候");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				final String responseString = arg1.body().string(); //雷：这个函数必须在后台线程中调用
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						try {
							PasswordChangeActivity.this.onResponse(arg0, responseString);
						} catch (Exception e) {
							e.printStackTrace();
							PasswordChangeActivity.this.onFailure(arg0, e);
						}
					}
				});
			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.dismiss();

						PasswordChangeActivity.this.onFailure(arg0, arg1);
					}
				});
			}
		});
	}

	void onResponse(Call arg0, String responseBody){
		new AlertDialog.Builder(this)
		.setTitle("注册成功")
		.setMessage(responseBody)
		.setPositiveButton("好", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		})
		.show();
	}

	void onFailure(Call arg0, Exception arg1) {
		new AlertDialog.Builder(this)
		.setTitle("请求失败")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("好", null)
		.show();
	}
}

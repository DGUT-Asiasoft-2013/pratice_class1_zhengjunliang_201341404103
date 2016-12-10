package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.api.Server;
import com.example.helloworld.entity.User;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
	SimpleTextInputCellFragment fragAccount,fragPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		


		fragAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);

		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRegister();
			}
		});

		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goLogin();
			}
		});

		findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goRecoverPassword();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		fragAccount.setLabelText("账户名");
		fragAccount.setHintText("请输入账户名");
		fragPassword.setLabelText("密码");
		fragPassword.setHintText("请输入密码");
		fragPassword.setIsPassword(true);
	}

	void goRegister(){
		Intent itnt = new Intent(this,RegisterActivity.class);
		startActivity(itnt);
	}

	void goLogin(){
		//		Intent itnt = new Intent(this, HelloWorldActivity.class);
		//		startActivity(itnt);

		String account = fragAccount.getText().toString();
		String password = fragPassword.getText().toString();

		// 以下为进度提示框
		final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setTitle("登录中");
		progressDialog.setMessage("请稍后");
		progressDialog.setCancelable(false); // 设置其不能取消
		progressDialog.setCanceledOnTouchOutside(false);
		// 开始
		progressDialog.show();

		// 创建客户端
		OkHttpClient client = Server.getSharedClient();

		// 把用户登录的信息传给服务器中标记好的String类型中
		MultipartBody body = new MultipartBody
				.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("account", account)
				.addFormDataPart("password", MD5.getMD5(password))
				.build();

		// 创建请求
		Request request = Server.requestBuilderWithApi("login")
				.post(body)
				.build();
			
		// 客户端发送一个请求newCall（），然后enqueue()进去对列，最后Callback()发送回连接的成功与否的信息
				client.newCall(request).enqueue(new Callback() {

					@Override
					public void onResponse(Call arg0, final Response arg1) throws IOException {
						
						
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								//获得解析数据
								User user;
								try {
									progressDialog.dismiss();
									ObjectMapper objectMapper=new ObjectMapper();
									user = objectMapper.readValue(arg1.body().string(), User.class);
									new AlertDialog.Builder(LoginActivity.this)
									.setTitle("登录成功")
									.setMessage(user.getName()+","+user.getAccount())
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent itnt=new Intent(LoginActivity.this, HelloWorldActivity.class);
											startActivity(itnt);
											finish();
										}
									})
									.show();
								} catch (JsonParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JsonMappingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}

					@Override
					public void onFailure(Call arg0, final IOException arg1) {
						LoginActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_LONG).show();

							}
						});
					}
				});
	}
	

	
	

	void goRecoverPassword(){
		Intent itnt = new Intent(this, PasswordRecoverActivity.class);
		startActivity(itnt);
	}
}
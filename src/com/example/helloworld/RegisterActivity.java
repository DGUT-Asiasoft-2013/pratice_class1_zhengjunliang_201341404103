package com.example.helloworld;

import java.io.IOException;

import com.example.helloworld.fragments.inputcells.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.example.helloworld.MD5;
import com.example.helloworld.api.Server;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	SimpleTextInputCellFragment fragInputCellEmailAddress;
	SimpleTextInputCellFragment fragInputCellName;
	PictureInputCellFragment fragInputAvatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellEmailAddress = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
		fragInputCellName = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_name);
		fragInputAvatar =  (PictureInputCellFragment) getFragmentManager().findFragmentById(R.id.input_picture);
		
		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();

			}
		});



	}

	@Override
	protected void onResume() {
		super.onResume();

		fragInputCellAccount.setLabelText("�û���");{
			fragInputCellAccount.setHintText("�������û���");
		}

		fragInputCellName.setLabelText("�ǳ�");{
			fragInputCellName.setHintText("�������ǳ�");
		}

		fragInputCellPassword.setLabelText("����");{
			fragInputCellPassword.setHintText("����������");
			fragInputCellPassword.setIsPassword(true);
		}

		fragInputCellPasswordRepeat.setLabelText("�ظ�����");{
			fragInputCellPasswordRepeat.setHintText("���ظ���������");
			fragInputCellPasswordRepeat.setIsPassword(true);
		}

		fragInputCellEmailAddress.setLabelText("��������");{
			fragInputCellEmailAddress.setHintText("�������������");
		}

	}

	void submit(){
		//--------------
		//��֤�������ظ������Ƿ���ͬ
		String password = fragInputCellPassword.getText();
		String passwordRepeat = fragInputCellPasswordRepeat.getText();

		if(!password.equals(passwordRepeat)){
			new AlertDialog.Builder(RegisterActivity.this)
			.setMessage("�������벻һ��Ŷ")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setNegativeButton("��", null)
			.show();
		}else{


			//---------------
			//�õ��ı�������
			String account = fragInputCellAccount.getText();
			String name = fragInputCellName.getText();
			String email = fragInputCellEmailAddress.getText();

			//---------------
			//����MD5
			password = MD5.getMD5(password);
			
			//����OKHttp�ͻ���

			MultipartBody.Builder requestBody = new MultipartBody.Builder()
					.setType(MultipartBody.FORM)
					.addFormDataPart("account", account)
					.addFormDataPart("name", name)
					.addFormDataPart("email", email)
					.addFormDataPart("passwordHash", password);

			
			
			OkHttpClient client = Server.getSharedClient();

			//----------------
			//�����洢ͼƬ
			byte[] pngData = fragInputAvatar.getPngData();
			if (pngData != null){
				RequestBody fileBody = RequestBody.create(MediaType.parse("image.png"), pngData);
				requestBody.addFormDataPart("avatar", "avatar.png", fileBody);
				
			}
			
			
			//����������
			MultipartBody newBody = requestBody.build();
			
			//�������󣬰�����ַ������("GET","POST","PUT","DELETE")
			Request request = Server.requestBuilderWithApi("register")
					.method("post", null)
					.post(newBody)
					.build();
			
			//---------------
			//ProgressDialog
			final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
			progressDialog.setMessage("�ȴ��У����Ժ�...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);



			client.newCall(request).enqueue(new Callback() {

				@Override
				public void onResponse(final Call arg0, final Response arg1) throws IOException {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							progressDialog.dismiss();

							try {
								RegisterActivity.this.onResponse(arg0,arg1);
							} catch (Exception e) {
								e.printStackTrace();
								RegisterActivity.this.onFailure(arg0, e);
							}

						}
					});

				}

				@Override
				public void onFailure(final Call arg0, final IOException arg1) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							RegisterActivity.this.onFailure(arg0, arg1);

						}
					});


				}
			});
		}
	}



	void onResponse(Call arg0, Response arg1) throws IOException{
		new AlertDialog.Builder(this)
		.setTitle("����ɹ�")
		.setMessage(arg1.body().string())
		.setNegativeButton("��", null)
		.show();

		

	}

	void onFailure(Call arg0, Exception e){
		new AlertDialog.Builder(this)
		.setTitle("����ʧ��")
		.setMessage(e.getLocalizedMessage())
		.setNegativeButton("��", null)
		.show();
	}


}

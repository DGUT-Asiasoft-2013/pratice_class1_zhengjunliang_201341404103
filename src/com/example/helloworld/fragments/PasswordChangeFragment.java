package com.example.helloworld.fragments;

import java.io.IOException;

import com.example.helloworld.R;
import com.example.helloworld.api.Server;
import com.example.helloworld.api.entity.User;
import com.example.helloworld.fragments.PasswordRecoverStep1Fragment.OnGoNextListener;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

public class PasswordChangeFragment extends Fragment {
	SimpleTextInputCellFragment fragPassword;
	SimpleTextInputCellFragment fragPasswordRepeat;
	SimpleTextInputCellFragment fragOldPassword;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
			
			fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
			fragPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
			fragOldPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);
			
			view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					submit();
				}
			});
		}
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		fragOldPassword.setLabelText("旧的密码");
		fragOldPassword.setHintText("请输入旧的密码");
		
		fragPassword.setLabelText("新密码");
		fragPassword.setHintText("输入新密码");
		
		fragPasswordRepeat.setLabelText("重复新密码");
		fragPasswordRepeat.setHintText("再次输入新密码");
	}
	
	
	
	public String getOldPassword(){
		return fragOldPassword.getText().toString();
	}
	
	public String getPassword(){
		return fragPassword.getText().toString();
	}
	
	public String getPasswordRepeat(){
		return fragPasswordRepeat.getText().toString();
	}
	
	public static interface OnSubmitListener{
		void onSubmit();
	}
	
	OnSubmitListener onSubmitListener;
	
	public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
		this.onSubmitListener = onSubmitListener;
	}
	
	void submit(){
		if(onSubmitListener!=null){
			onSubmitListener.onSubmit();
		}
	}
}

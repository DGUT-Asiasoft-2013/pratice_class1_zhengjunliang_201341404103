package com.example.helloworld;

import com.example.helloworld.fragments.inputcell.PictureInputCellFragment;
import com.example.helloworld.fragments.inputcell.SimpleTextInputCellFragment;

import android.app.Activity;
import android.os.Bundle;

public class RegisterActivity extends Activity {
	
	SimpleTextInputCellFragment fragInputCellAccount;
	SimpleTextInputCellFragment fragInputCellPassword;
	SimpleTextInputCellFragment fragInputCellPasswordRepeat;
	PictureInputCellFragment fragInputCellPicture;
	SimpleTextInputCellFragment fragInputCellEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
		fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
		fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
		fragInputCellPicture = (PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_picture);
	
		fragInputCellEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		fragInputCellAccount.setLabelText("用户名");
		fragInputCellAccount.setHintText("请输入用户名");
		
		fragInputCellEmail.setLabelText("Email");
		fragInputCellEmail.setHintText("请输入Email");
		
		fragInputCellPassword.setLabelText("密码");
		fragInputCellPassword.setHintText("请输入密码");
		fragInputCellPassword.setIsPassword(true);
		
		fragInputCellPasswordRepeat.setLabelText("重复密码");
		fragInputCellPasswordRepeat.setHintText("请再次输入密码");
		fragInputCellPasswordRepeat.setIsPassword(true);
		
	}
	
}

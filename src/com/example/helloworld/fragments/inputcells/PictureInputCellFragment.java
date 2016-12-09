package com.example.helloworld.fragments.inputcells;

import java.io.ByteArrayOutputStream;

import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureInputCellFragment extends BaseInputCellFragment {
	
	final int REQUEST_CAMERA = 1;
	final int REQUEST_ALBUM = 0;
	
	ImageView imageView;
	TextView labelText;
	TextView hintText;
	
	byte[] pngData;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);
		
		imageView = (ImageView) view.findViewById(R.id.image);
		labelText = (TextView) view.findViewById(R.id.label);
		hintText = (TextView) view.findViewById(R.id.hint);
		
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();		
			}
		});
		
		
		return view;
	}
	

	

	void onImageViewClicked(){
		
		String[] items = {
				"拍照",
				"相册"
		};
		
		new AlertDialog.Builder(getActivity())
		.setTitle(labelText.getText())
//		.setMessage(hintText.getText())
		.setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				switch (which) {
				case 0:
					takePhoto();
					break;
				
				case 1:
					pickFromAlbum();
					break;
					
				default:
					break;
				}
			}
		})
		.setNegativeButton("取消", null)
		.show();
		}
	void takePhoto(){
		Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(itnt, REQUEST_CAMERA);
		
	}
	void pickFromAlbum(){
		Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);
		itnt.setType("image/*");
		startActivityForResult(itnt, REQUEST_ALBUM);
	}
	
	void saveBitmap(Bitmap bmp){
		ByteArrayOutputStream bStream = new ByteArrayOutputStream(); 
		bmp.compress(CompressFormat.PNG, 100, bStream);
		pngData = bStream.toByteArray();
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_CANCELED) return;

		if(requestCode == REQUEST_CAMERA){
			
			Bitmap bmp = (Bitmap)data.getExtras().get("data");
			imageView.setImageBitmap(bmp);
			//保存图片
			saveBitmap(bmp);
			
//			Object dataObj = data.getExtras().get("data");
//			Log.d("camera data", dataObj.getClass().toString());
			
//			for(String key : data.getExtras().keySet()){
//				Log.d("camera capture", key);
//			}
//			Log.d("camera capture",data.getExtras().keySet().toString());
//			Toast.makeText(getActivity(), data.getDataString(), Toast.LENGTH_LONG).show();
		}else if(requestCode == REQUEST_ALBUM){
			try{
			Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
			imageView.setImageBitmap(bmp);
			//保存图片
			saveBitmap(bmp);
			
			}catch(Exception e){
				e.printStackTrace();
			}
//			Uri dataUri = data.getData();			
//			Toast.makeText(getActivity(), dataUri.getClass().toString(), Toast.LENGTH_LONG).show();
//		
//		    Object obj = data.getExtras().get("data");
//			Toast.makeText(getActivity(), obj.getClass().toString(), Toast.LENGTH_LONG).show();
			
		}

	}
	
	//让其他类可以获得pngData
	public byte[] getPngData(){
		return pngData;
	}
	
	@Override
	public void setLabelText(String labelText) {
		this.labelText.setText(labelText);
	}

	@Override
	public void setHintText(String hintText) {
		this.hintText.setText(hintText);
		
	}
}

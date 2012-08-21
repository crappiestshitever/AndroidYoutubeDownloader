package yt.video.download;

import System.AppChecker;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Handler;
import debugger.ShowMessage;
import debugger.debugger;

public class VideoPlay {
	
	private String Location; 
	private Activity acc;
	public VideoPlay(String Location, Activity acc)
	{
		this.acc = acc;
		this.Location = Location; 
	}
	private boolean CanPlay()
	{
		if (Location.toLowerCase().endsWith(".webm") || Location.toLowerCase().endsWith(".flv"))
		{
			boolean VideoPlayer_exists = false;
			AppChecker checker = new AppChecker("com.mxtech.videoplayer.ad", acc); 
			if (checker.check())
				VideoPlayer_exists = true; 
			checker = new AppChecker("com.mxtech.videoplayer.pro", acc); 
			if (checker.check())
				VideoPlayer_exists = true; 
			ShowMessage message = new ShowMessage(acc);
			if (!VideoPlayer_exists)
			{
				message.show("You ain´t got no video-playback app installed that support this video-format (like MX Video Player)");
				message = null; 
				return false; 
			}
			else 
				return true; 
		}
		else 
			return true; 
	}
	public void playVid() {
		// TODO Auto-generated method stub
		//if (CanPlay())
		debugger.debug("Loction: "+"file://"+Location);
		openFile(Uri.parse("file://"+Location));
//		else 
//			new ShowMessage(acc).show("Cannot play the Vid!");
	}
	
	void openFile(Uri uri)
  	{
		/**
	  	Intent intent = new Intent();
	  	intent.setAction(android.content.Intent.ACTION_RUN);
	  	intent.setDataAndType(uri, "video/*");
	  	try {
	  	acc.startActivity(intent);
	  	}catch (Exception e)
	  	{
	  		ShowMessage message = new ShowMessage(acc);
	  		message.showSecs("Cant handle this video format, not application installed?", 6); 
	  		e.printStackTrace(); 
	  		message = null; 
	  	}

	  	intent = null; 
	  	**/ 
	 
	  	try {
	  	Intent intent =  new Intent(Intent.ACTION_DEFAULT, uri);
	 	intent.setDataAndType(uri, "video/*");
	  	acc.startActivity(Intent.createChooser(intent, "Please choose the playback app!"));
	  	}catch (Exception e2)
	  	{
	  		ShowMessage message = new ShowMessage(acc);
	  		message.showSecs("Failed to open 'open with' dialog!", 6); 
	  		e2.printStackTrace(); 
	  		message = null; 
	  	}
  	}
	public void start_file_open(){
		
		final AlertDialog.Builder alert = new AlertDialog.Builder(acc);
	
		alert.setTitle("Wollen Sie das Video nun oeffnen ?");
		//alert.setIcon(R.drawable.logo_small);
		alert.setPositiveButton("YES",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  playVid(); 
			  acc.onBackPressed();
		  }
		 });
		alert.setNegativeButton("NO",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  acc.onBackPressed();
		  }
		 });
		Handler handler = new Handler(); 
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				alert.show();
			}
		});
	
	}
	
	private void AndroidVersionNotSupported()
	{
		ShowMessage message = new ShowMessage(acc); 
		message.show("Your Android-Version does not support this action!");
		message = null; 
	}
	private void playVidUrl(String d_link)
	{
		//get the DownloadLink
		final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(d_link));
		try {
			acc.startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new ShowMessage(acc).show("There is no default VideoPlayer listed"); 
			e.printStackTrace();
		}
	}
}

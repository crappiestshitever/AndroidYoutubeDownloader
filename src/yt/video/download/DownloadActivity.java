package yt.video.download;

import java.net.MalformedURLException;
import java.net.URL;

import System.System;
import Youtube.VideoBase;
import Youtube.Youtube;
import Youtube.http_download;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import debugger.ShowMessage;
import debugger.debugger;

public class DownloadActivity extends Activity implements OnClickListener{
	
	TextView text_info, edit_title;
	Button b_download;
	Youtube tube; 
	Spinner spinner;
	ImageView thumbnail; 
	private String DownloadLink = ""; 
	Bundle bundle; 
	CheckBox box;
	Activity me = this; 
	protected int FreeSpace = 0; 
	boolean SDCard = false; 
	protected int Video_MBSize = 0; 
	VideoBase base;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.download);
        base = new VideoBase(this.getApplicationContext()); 
       
        
        Bundle bundle = getIntent().getExtras();
        this.bundle = bundle;
        init(bundle); 
    }
	 @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
 
    }
 	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle item selection
      switch(item.getItemId()){
      
	      case R.id.VideoList:
	      {
	    	  Bundle bundle = new Bundle();
	    	  bundle.putStringArray("SD", base.getFilesFromStorage(VideoBase.SDCard));
	    	  bundle.putStringArray("Internal", base.getFilesFromStorage(VideoBase.Internal));
	    	  
	    	  Intent intent = new Intent(DownloadActivity.this, VideoListViewer.class);
	    	  intent.putExtras(bundle);
	    	  DownloadActivity.this.startActivity(intent);
	      }
    }
	return false;
    }
 	public void createdefaultDialog()
 	{
 		final CharSequence[] items = {"Red", "Green", "Blue"};

 		AlertDialog.Builder builder = new AlertDialog.Builder(this);
 		builder.setTitle("Pick a color");
 		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
 		    public void onClick(DialogInterface dialog, int item) {
 		        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
 		    }
 		});
 		AlertDialog alert = builder.create();
 	}
	private void init(Bundle bundle)
	{
		tube = new Youtube(bundle.getString("URL"), this);
		tube.parseUrl();
		tube.setDefaultList(bundle.getStringArrayList("default_list"));
		edit_title = (TextView)findViewById(R.id.edit_video_title);
		edit_title.setText(bundle.getString("title"));
		
		box = (CheckBox)findViewById(R.id.check_SDCard);
	
		text_info = (TextView)findViewById(R.id.text_info);
		text_info.setText("URL="+bundle.getString("URL"));
    	b_download= (Button )findViewById(R.id.b_download);
    	b_download.setOnClickListener(this);
    	spinner = (Spinner) findViewById(R.id.choice_quali);
    	ArrayAdapter adapter = new ArrayAdapter(this ,android.R.layout.simple_list_item_1, bundle.getStringArrayList("quali").toArray());
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(adapter);
    	
//    	thumbnail = (ImageView)findViewById(R.id.imageView1);
//    	thumbnail.setOnClickListener(this);
//    	showJpg(bundle.getString("thumbnail-path"), thumbnail);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == b_download)
		{
			
			final ProgressDialog pd= ProgressDialog.show(this,
					"Please wait...", 
					"getting information", 
					true, // zeitlich unbeschränkt 
					false // nicht unterbrechbar
				); 
				final Handler handler = new Handler() {
		            @Override
		            public void handleMessage(Message msg) {
		                    if (msg.what == 0)
		                    {
		                    	pd.dismiss();
		                    	String AlertMessage = "Would you like to download a "+String.valueOf(Video_MBSize) +"MB video? (Free: "+String.valueOf(FreeSpace)+"MB )";
		                    	show_alert_dialog(me, AlertMessage, SDCard);
		                    }else 
		                    {
		                    	pd.dismiss();
		                    
		                    }
		                    //do something
		            }
				};
			
			
			Thread try_download_thrad = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SDCard = false; 
					ShowMessage message = new ShowMessage(me); 
					String quali = (String)spinner.getSelectedItem();
					int FormatCode = tube.getFormatCode(quali);
					DownloadLink = tube.getVideoLink(FormatCode);
					   debugger.debug("Download="+DownloadLink); 
					if (tube.isValid())
					{
						Video_MBSize = tube.getVideoSizeinMB(DownloadLink);
						if (box.isChecked())//SD-Card storage 
						{
							SDCard = true; 
							FreeSpace = (int)new System().getAvailiableFreeSpaceOnSDCardInMB();
						}
						else 
							FreeSpace = (int)new System().getAvailiableFreeSpaceOnIntenalStorage();
						
						if (FreeSpace<=Video_MBSize)
						{
							message.show("You do not have enough free space");
							handler.sendEmptyMessage(1);
						}
						else 
						{
							handler.sendEmptyMessage(0);
							
						}
					}
					else handler.sendEmptyMessage(1);
				}
			}); 
			try_download_thrad.start(); 
			
		}
		
		else if (v == thumbnail)
		{
			
		}
	}
	public void show_alert_dialog(Activity acc, String Message, final boolean SDCard) {

		final AlertDialog.Builder alert = new AlertDialog.Builder(acc);

		alert.setTitle("Would you like to download the video?");
		//alert.setIcon(R.drawable.logo_small);
		alert.setMessage(Message);
		alert.setPositiveButton("YES",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
		   do_download(SDCard); 
		  }});
		alert.setNegativeButton("NO",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  //do_nothing
		  }
		 });
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				alert.show(); 
			}
		});
	}
	private void do_download(boolean SDCard) {
		// TODO Auto-generated method stub
		try {
			String selected = (String)spinner.getSelectedItem();
			String endung = ""; 
			
			if (selected.contains("mp4"))
			{
				endung = "mp4";
			}
			else if (selected.contains("flv"))
				endung = "flv";
			else if (selected.contains("webm"))
			{
				endung = "webm";
			}
			/*
			 * Start the Download
			 */
			http_download download = new http_download(new URL(DownloadLink), this.getApplicationContext(), this, System.correctFileName(bundle.getString("title")+"."+endung), SDCard);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void showJpg(String Location, ImageView view)
	{
	  BitmapFactory.Options options = new BitmapFactory.Options();
	  options.inSampleSize = 2;
	  Bitmap bm = BitmapFactory.decodeFile(Location, options);
	  view.setImageBitmap(bm);
	  view.refreshDrawableState();
	}
}

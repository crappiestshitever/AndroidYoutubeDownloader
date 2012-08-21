package yt.video.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import debugger.ShowMessage;
import debugger.debugger;

import Youtube.VideoBase;
import Youtube.Youtube;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Yt_video_downloadActivity extends Activity implements OnClickListener{
    /*
     * To do: 
     * serverping am Anfang gehaxelt.in erreichbar?
     */
	Button b_next;
	EditText edit_url;
	private String URL;
	private ArrayList<String> quali;
	private String title; 
	Youtube tube;
	private File Thumbnail_file = null;
	private URL thumbnail_url = null;
	Yt_video_downloadActivity me = this; 
	private boolean thumbnail_ready = false;
	private int thumbnail_counter = 0; 
	WebView view;
	private VideoBase base;
	/** Called when the activity is first created. */
	
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOnline())
        {
        	if (ping(data.RunPhpLocation.replace("?url=", "")))
        	{
		        base = new VideoBase(this.getApplicationContext()); 
		        base.putTestData();
		        getWindow().requestFeature(Window.FEATURE_PROGRESS);
		        setContentView(R.layout.main);
		        init();
        	}
        	else
        	{
        		ShowMessage msg=new ShowMessage(this);
            	msg.show(getResources().getString(R.string.EScriptOffline));
            	beenden();
        	}
        }
        else
        {
        	ShowMessage msg=new ShowMessage(this);
        	msg.show(getResources().getString(R.string.ENoInternetAccess));
        	beenden();
        }
    }
    
    private boolean ping(String target)
    {
    	HttpURLConnection connection = null;
        try {
            URL u = new URL(target);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            if (code==200)
            {
            	return true;
            }
            else
            {
            	return false;
            }
            // You can determine on HTTP return code received. 200 is success.
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
    
    
    public void onBackPressed()
    {
    	debugger.debug(view.getUrl());
    	if (!view.getUrl().startsWith("http://m.youtube.com/index") && !view.getUrl().startsWith("http://m.youtube.com/home"))
		{
    		view.goBack();
		}	
    	else 
    	{
    		beenden(); 
    	}
    		
    }
    
 	
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
 
    }
 	
 	
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle item selection
      switch(item.getItemId()){
      
	      case R.id.VideoList:
	      {
	    	  Bundle bundle = new Bundle();
	    	  bundle.putStringArray("SD", base.getFilesFromStorage(VideoBase.SDCard));
	    	  bundle.putStringArray("Internal", base.getFilesFromStorage(VideoBase.Internal));
	    	  
	    	  Intent intent = new Intent(Yt_video_downloadActivity.this, VideoListViewer.class);
	    	  intent.putExtras(bundle);
	    	  Yt_video_downloadActivity.this.startActivity(intent);
	      }
      }
      return false;
    }
    
	public void beenden() 
	{
		  Intent result = new Intent("Complete");
          setResult(Activity.RESULT_OK, result);
          finish(); 
	}
	
    private void init()
    {
    	b_next = (Button)findViewById(R.id.b_next);
    	b_next.setOnClickListener(this);
    	view = (WebView)findViewById(R.id.webView1);
    	view.canGoBack();
    	
    	view.goBack();
    	view.getSettings().setJavaScriptEnabled(true);

    	 final Activity activity = this;
    	 view.setWebChromeClient(new WebChromeClient() {
    	   public void onProgressChanged(WebView view, int progress) {
    	     // Activities and WebViews measure progress with different scales.
    	     // The progress meter will automatically disappear when we reach 100%
    	     activity.setProgress(progress * 1000);
    	   }
    	 });
    	 view.setWebViewClient(new WebViewClient() {
    	   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
    	   }
    	 });
    	view.loadUrl("http://www.youtube.com/"); 
    }
    
    public boolean isOnline()
	{
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting())
		    {
		    	return true;
		    }
		    return false; 
	}
    
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == b_next)
		{
			//URL = edit_url.getText().toString();
			if (view.getUrl().contains("v="))
			{
				String VideoID = view.getUrl().split("v=")[1];
				
				URL = "http://www.youtube.com/watch?v="+VideoID;
				tube = new Youtube(URL, this);
				final ShowMessage message = new ShowMessage(this);
				
				final ProgressDialog pd= ProgressDialog.show(this,
						"Please wait...", 
						"getting information", 
						true, // zeitlich unbeschränkt 
						false // nicht unterbrechbar
					); 
					final Handler handler = new Handler() {
			            
			            public void handleMessage(Message msg) {
			                    if (msg.what == 0)
			                    {
			                    	pd.dismiss();
			                    	thumbnail_ready = false; 
			                    	startNextActivity();
			                    }else 
			                    {
			                    	pd.dismiss();
			        
			                    }
			                    //do something
			            }
					};
				Thread t=new Thread()
				{
					public void run()
					{
						
						if (!tube.parseUrl())
						{
							message.show("Error:could not parse URL");
							handler.sendEmptyMessage(1);
						}
						else 
						{
							if (isOnline())
								title = tube.getTitle();
							else 
								handler.sendEmptyMessage(1);
							
							if (isOnline())
								quali = tube.getVideoFormats();
							else 
								handler.sendEmptyMessage(1);
							
							handler.sendEmptyMessage(0);
							/** Thumnail Load **/
							/**
							if (isOnline())
								loadThumbnail(); 
							else 
								handler.sendEmptyMessage(1);
							
							if (thumbnail_ready==true)
								handler.sendEmptyMessage(0);
							else 
							{
							message.show("Could not download the information!");
								handler.sendEmptyMessage(1);
							}
							**/ 
							
						}
		
					}
					
				};
				t.start();
			}
			else 
			{
				new ShowMessage(this).showLong("Please select a video!"); 
			}
		}
		
	}
	public void loadThumbnail()
	{
		String ThumbnailLink = tube.getThumbnailLink();
		debugger.debug(ThumbnailLink);
		try {
			thumbnail_url = new URL(ThumbnailLink);
			debugger.debug(thumbnail_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread t = new Thread(new Runnable() {
			
		

			
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpURLConnection urlConnection = (HttpURLConnection) thumbnail_url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoOutput(true);
					urlConnection.connect();
					File SDCardRoot = Environment.getExternalStorageDirectory();
					Thumbnail_file = new File(SDCardRoot,"__thumbnail_yt_0"+String.valueOf(thumbnail_counter) +".jpg");
					FileOutputStream fileOutput = new FileOutputStream(Thumbnail_file);
					InputStream inputStream = urlConnection.getInputStream();
					int totalSize = urlConnection.getContentLength();
					
					int bufferLength = 0; 
					int downloadedSize = 0; 
					double ges2 = 0;
					byte[] buffer = new byte[1024];
					while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
					      fileOutput.write(buffer, 0, bufferLength);
					      downloadedSize += bufferLength;
					}
					double new1 = (double)totalSize; 
					double new2 = (double)downloadedSize; 
					ges2 = (new2/new1)*100;
					ShowMessage message = new ShowMessage(me);
					if (ges2 == 100)
						thumbnail_ready = true;
					thumbnail_counter++;
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}); 
		t.start(); 
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 private void startNextActivity()
	    {
	    	Intent intent = new Intent(Yt_video_downloadActivity.this, DownloadActivity.class);
	    	
	    	Bundle bundle = new Bundle();
	    	
	    	bundle.putString("URL", URL);
	    	bundle.putStringArrayList("quali", quali);
	    	bundle.putStringArrayList("default_list", tube.getDefaultList());
	    	bundle.putString("title", title); 
//	    	bundle.putString("thumbnail-path", Thumbnail_file.getAbsolutePath());
	    	intent.putExtras(bundle);
	    	
	    	//Start next activity
	    	Yt_video_downloadActivity.this.startActivity(intent);
	    }
    
}
package Youtube;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import yt.video.download.VideoPlay;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import debugger.ShowMessage;
import debugger.debugger;

  public class http_download{
	  public  ProgressDialog progressDialog;
	  private boolean startet = false;
	  int bufferLength = 0; 
	  byte[] buffer; 
	  int downloadedSize; 
	  InputStream inputStream; 
	  int totalSize; 
	  FileOutputStream fileOutput; 
	  Context context;
	  double ges2 = 0;
	  Activity acc; 
	  private Handler mHandler;
	  private String FileName = ""; 
      File file = null;
      
	   public http_download(final URL url, final Context context, final Activity acc, String FileName, boolean sDCard){
			
		   this.context = context; 
	
			this.acc = acc;
			try {
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		        urlConnection.setRequestMethod("GET");
		        urlConnection.setDoOutput(true);
		        urlConnection.connect();
		        File ROOTDirectory =null;

		        if (sDCard)
		        {
		        	ROOTDirectory = Environment.getExternalStorageDirectory();
		    		file = new File(ROOTDirectory, FileName);
		    	    fileOutput = new FileOutputStream(file);
		        }
		        else 	
		        {
//		        	String filePath = context.getFilesDir().getPath().toString() + "/"+FileName;
		        	file = new File(FileName);
		        	debugger.debug("Der absolute Pfad zum Video liegt hier: "+file.getAbsolutePath()); 
		        	fileOutput = context.openFileOutput(FileName, Context.MODE_PRIVATE); 
		        }
		    	new ShowMessage(acc).showSecs("PFAD="+file.getAbsolutePath(), 10); 
		    	
		        inputStream = urlConnection.getInputStream();
		        totalSize = urlConnection.getContentLength();
		    	//progressDialog.setMax(totalSize); 
		        
		        final ProgressDialog pd = ProgressDialog.show(acc, 
		     			"Bitte Warten...", 
		     			String.valueOf(ges2)+"% heruntergeladen",
		     			true,
		     			true // is cancelable
		     	);
				final Runnable changeMessage = new Runnable() {
				    //@Override
				    public void run() {
				    	//Log.v("HTTP_DOWNLOAD",String.valueOf((int)ges2)+"% heruntergeladen");
				        pd.setMessage(String.valueOf((int)ges2)+"% heruntergeladen");
				    }
				};
				final Handler handler = new Handler() {
		            @Override
		            public void handleMessage(Message msg) {
		            	pd.dismiss();
		            
				        new VideoPlay(file.getAbsolutePath(), acc).start_file_open();
		            }
				};
				Thread t= new Thread()
				{
					@Override
					public void run()
					{
						try
						{
					        int downloadedSize = 0; 
					        buffer = new byte[1024];
					        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
								  double new1 = (double)totalSize; 
								  double new2 = (double)downloadedSize; 
								  ges2 = (new2/new1)*100;  
								  //Thread.sleep(100);
								  acc.runOnUiThread(changeMessage);
							      fileOutput.write(buffer, 0, bufferLength);
							      downloadedSize += bufferLength;
							      //Thread.sleep(100);
					        }
					        fileOutput.close();
					        handler.sendEmptyMessage(0);
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				};
				t.start();
		   }catch (Exception e){
			   e.printStackTrace(); 
		   }
	}
	   
	
	
}

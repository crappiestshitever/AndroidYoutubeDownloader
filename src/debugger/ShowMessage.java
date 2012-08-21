package debugger;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ShowMessage{

	public String s = ""; 
	public Activity acc; 
	public ShowMessage(Activity acc)
	{
		this.acc = acc; 
	}
	  public void show(final String s)
	  {
		  this.s = s; 
		 Handler handler = new Handler(Looper.getMainLooper());
		 handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(acc.getApplicationContext(), s , Toast.LENGTH_SHORT).show();
			
			}
		});
		
	  }
	  public void showLong(final String s)
	  {
		  this.s = s; 
		 Handler handler = new Handler(Looper.getMainLooper());
		 handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(acc.getApplicationContext(), s , Toast.LENGTH_LONG).show();
			
			}
		});
		
	  }
	  public void showSecs(final String s, final int secs)
	  {
		  this.s = s; 
		 Handler handler = new Handler(Looper.getMainLooper());
		 handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(acc.getApplicationContext(), s , secs).show();
			
			}
		});
		
	  }
}

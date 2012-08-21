package System;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AppChecker {
	
	private String Package = ""; 
	private Activity acc;
	public AppChecker(String Package, Activity acc)
	{
		this.Package = Package;
		this.acc = acc; 
	}
	public boolean check()
	{
		Intent intent = new Intent(Package); 
		return isCallable(Package); 
	}
	private boolean isCallable(String URI)
	{
			PackageManager pm = acc.getPackageManager();
			boolean app_installed = false;
			
			try {
				pm.getPackageInfo(URI, PackageManager.GET_ACTIVITIES);
				app_installed = true;
				
			} catch (PackageManager.NameNotFoundException e) {
				// TODO Auto-generated catch block
				app_installed = false;
				e.printStackTrace();
			}
			return app_installed ;
	}
	
}

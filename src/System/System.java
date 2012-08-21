package System;

import java.io.File;




import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class System {
	
	public System()
	{
		
	}
	public double getAvailiableFreeSpaceOnSDCardInMB()
	{
			String status = Environment.getExternalStorageState();
			
		  if (status.equals(Environment.MEDIA_MOUNTED)) {

			  	StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
			  	double sdAvailSize = (double)stat.getAvailableBlocks() *(double)stat.getBlockSize();
			  	//One binary gigabyte equals 1,073,741,824 bytes.
			  	double gigaAvailable = sdAvailSize / 1073741824;
			  	return gigaAvailable*1000;//Mega availiable
		  }
		  else return -1.00;
	}
	public double getAvailiableFreeSpaceOnIntenalStorage()
	{
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		
		double sdAvailSize = (double)stat.getAvailableBlocks() *(double)stat.getBlockSize();
	  	//One binary gigabyte equals 1,073,741,824 bytes.
	  	double gigaAvailable = sdAvailSize / 1073741824;
	  	return gigaAvailable*1000;//Mega availiable
	}
	public static String correctFileName(String name)
	{
		String tmp = name.replace("\"", " ").replace(".", "-");
		int lastIndex = tmp.lastIndexOf("-");
		String extention = tmp.substring(lastIndex).replace("-", ".");
		tmp = tmp.replace(tmp.substring(lastIndex), extention);
		return tmp;
	}
	public static String getAndroidVersion()
	{
		return String.valueOf(Build.VERSION.RELEASE);
	}
}

package Youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import yt.video.download.data;

import android.content.Context;
import debugger.debugger;

public class VideoBase implements data {
	

	private File Base = new File(BASENAME); 
	private FileOutputStream out_stream;
	private FileInputStream in_stream;
	private BufferedReader reader; 
	private PrintWriter writer; 
	private Context t; 
	
	public VideoBase(Context t)
	{
		this.t = t; 
		create(); 
	
	}
	public boolean create()
	{
		try {
			return Base.createNewFile() ? true : false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	public String getPath(String key)
	{
		try {
			in_stream = t.openFileInput(BASENAME);
			reader = new BufferedReader(new InputStreamReader(in_stream),8 * 1024);
			String line = ""; 
			
				while ((line = reader.readLine()) != null)
				{
					if (line.split("##")[1].equals(key))
						return line.split("##")[2];
				}
				if (reader!=null)
					reader.close();
				if (in_stream!=null)
					in_stream.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (reader!=null)
					reader.close();
				if (in_stream!=null)
					in_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try { 
				if (reader!=null)
					reader.close();
				if (in_stream!=null)
					in_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			return null;
		}
		return null;
	}
	/*
	 * @return value could be null
	 */
	public String[] getFilesFromStorage(String Storage)
	{
		try {
			in_stream = t.openFileInput(BASENAME);
			reader = new BufferedReader(new InputStreamReader(in_stream), 8 * 1024);
			String line = ""; 
			ArrayList<String> files = new ArrayList<String>(); 
			
				while ((line = reader.readLine()) != null)
				{
					debugger.debug(line);
					if (line.contains("##"))
					{
						if (line.split("##")[0].equals(Storage))
							 files.add(line.split("##")[1]);
					}
				}
				in_stream.close(); 
				reader.close(); 
				debugger.debug(files.toArray());
				
				if (files.size()!=0)
				{
					String[] array = new String[files.size()];
					for (int i = 0; i<files.size(); i++)
						array[i] = files.get(i);
						
					return array;
				}
				else 
					return null; 
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (reader!=null)
					reader.close();
				if (in_stream!=null)
					in_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				if (reader!=null)
					reader.close();
				if (in_stream!=null)
					in_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			return null;
		}
	}
	public void putTestData()
	{
		 this.put("newFile.avi", "\\sdcard\\newFile.avi", VideoBase.SDCard); 
		 this.put("newFile2.avi", "\\sdcard\\newFile2.avi", VideoBase.SDCard); 
		 this.put("newFile3.avi", "\\sdcard\\newFile3.avi", VideoBase.SDCard); 
	        
		 this.put("newFiles.avi", "\\newFiles.avi", VideoBase.Internal); 
	     this.put("newFiles2.avi", "\\newFiles2.avi", VideoBase.Internal); 
	     this.put("newFiles3.avi", "\\newFiles3.avi", VideoBase.Internal); 
	}
	public void put(String key, String entry, String Storage)
	{
		try {
			out_stream = t.openFileOutput(BASENAME, Context.MODE_APPEND);
			
			String data = Storage + "##" + key + "##" + entry + "\n";
			debugger.debug("put data?"+data);
			writer = new PrintWriter(out_stream); // AppendMode on
			writer.println(data);
			writer.flush();
			writer.close();
			
			out_stream.close(); 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete(String key)
	{
		
	}
}
	


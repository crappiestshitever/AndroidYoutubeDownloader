package Youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlToString {
	private String Content = ""; 
	public UrlToString(String URL)
	{
		try {
			URL url = new URL(URL);
			URLConnection connection = url.openConnection();
			Content = inputStreamToString(connection.getInputStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	public String getContent()
	{
		return this.Content; 
	}
	private String inputStreamToString(InputStream is) throws IOException {
	    String s = "";
	    String line = "";
	    
	
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is), 8 * 1024);
	    
	    while ((line = rd.readLine()) != null) {
	    	s += line; 
	    	//Log.v(TAG,line); 
	    }
	    if (s.equals(""))
	    	return null;
	    return s;
	}
}

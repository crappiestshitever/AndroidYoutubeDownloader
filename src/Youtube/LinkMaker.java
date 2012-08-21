package Youtube;

import java.net.MalformedURLException;
import java.net.URL;

import yt.video.download.data;

public class LinkMaker implements data {
	

	

	/*
	 * spezific
	 */
	private int Format = 0;
	private String LINK = ""; 
	private String VideoUrl =""; 
	private String Option = ""; 
	public LinkMaker(String VideoURL, String Option)
	{
		this.VideoUrl = VideoURL;
		this.Option = Option; 
		make(); 
	}
	public void make()
	{
		if (Option.equals(FORMAT))
		{
			LINK = RunPhpLocation+VideoUrl+"&"+"format="+String.valueOf(Format)+"&info="+FORMAT; 
		}
		else if(Option.equals(GET_LINK))
		{
			LINK = RunPhpLocation+VideoUrl;
		}
		else 
		{
			LINK = RunPhpLocation+VideoUrl+"&"+"info="+Option;
		}
	}
	public void setFormat(int Format)
	{
		this.Format = Format; 
	}
	public String getLink()
	{
		return this.LINK;
	}
	public URL getURL()
	{
		try {
			return new URL(LINK);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

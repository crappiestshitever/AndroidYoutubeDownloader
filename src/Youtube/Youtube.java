package Youtube;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import debugger.debugger;

import android.app.Activity;

public class Youtube {
	
	private String link;
	private Activity acc; 
	private ArrayList<String> defaultList = null;
	
	public Youtube(String link, Activity acc)
	{
		this.link = link;
		this.acc = acc; 
	}
	public boolean parseUrl()
	{ 
		if (!link.startsWith("http"))
			return false; 
		try {
			link = URLEncoder.encode(link);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; 
			
		}
		return true;
		
	}
	public ArrayList<String> getDefaultList()
	{
		return this.defaultList;
	}
	public void setDefaultList(ArrayList<String> arr)
	{
		this.defaultList = arr;
	}
	public ArrayList<String> getVideoFormats()
	{
	
		LinkMaker maker = new LinkMaker(link, LinkMaker.GET_FORMATS);
		UrlToString urlToString = new UrlToString(maker.getLink());
		return parseVideoFormats(urlToString.getContent());
	}
	public String getTitle()
	{
		LinkMaker maker = new LinkMaker(link, LinkMaker.GET_TITLE);
		UrlToString urlToString = new UrlToString(maker.getLink());
		String title = urlToString.getContent();
		return title; 
	}
	public String getSimpleVideoLink()
	{
		LinkMaker maker = new LinkMaker(link, LinkMaker.GET_LINK);
		UrlToString urlToString = new UrlToString(maker.getLink());
		String content = urlToString.getContent();
		String[] splitContent = content.split("http");
		return "http"+splitContent[1].trim();
	}
	private ArrayList<String> parseVideoFormats(String string)
	{
		ArrayList<String> list = new ArrayList<String>(); 
		debugger.debug(string);  
		String[] split = string.split("Available formats:");
		String[] split_quali = split[1].split(":");
		for (String s : split_quali)
		{
			if (s.contains("]") && s.contains("["))
			{			
				String[] newSplit = s.split("]");
				for (String splitString : newSplit)
					list.add(splitString.replace("[", " "));
			}
			else 
			{
				list.add(s.replace("]", "").replace("[", " "));
			}
		}
		defaultList = (ArrayList<String>) list.clone();
		for (int i = 0; i<list.size();i++)
		{
			if (!list.get(i).contains("x"))
			{
				list.remove(i);
				i=0;
			}
		}
		debugger.debug(list.toArray());
		debugger.debug(defaultList.toArray());
		
		return list;
	}
	public boolean isValid()
	{
		LinkMaker maker = new LinkMaker(link, LinkMaker.IS_VALID);
		UrlToString urlToString = new UrlToString(maker.getLink());
		String content = urlToString.getContent();
		if (content.contains("true"))
		{
			return true; 
		}
		else return false; 
	}
	public String getVideoLink(int FormatCode)
	{
		LinkMaker maker = new LinkMaker(link, LinkMaker.FORMAT);
		maker.setFormat(FormatCode);
		maker.make();
		UrlToString urlToString = new UrlToString(maker.getLink());
		String content = urlToString.getContent();
		return getDefaultLink(content); 
	}
	public int getVideoSizeinMB(String File)
	{
		URLConnection connection;
		try {
			connection = new URL(File).openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return (int)(connection.getContentLength()/1000000);
	}
	public String getThumbnailLink()
	{
		LinkMaker maker = new LinkMaker(link, LinkMaker.GET_THUMBNAIL);
		UrlToString urlToString = new UrlToString(maker.getLink());
		String content = urlToString.getContent();
		return getDefaultLink(content); 
	}
	public int getFormatCode(String Element)
	{
		if (defaultList!=null)
		{
			for (int i = 0; i<defaultList.size();i++)
			{
				if (defaultList.get(i).equals(Element))
				{
					return Integer.parseInt(defaultList.get((i-1)).trim().replace(" ", ""));
				}
			}
		}
		else return -1;
		return -1;
	}
	private String getDefaultLink(String content)
	{
		String[] splitContent = content.split("http");
		return "http"+splitContent[1].trim();
	}
}

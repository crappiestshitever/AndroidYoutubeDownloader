package yt.video.download;

import android.os.IBinder;
import android.os.IInterface;

public interface data {
	/*
	 * database
	 */
	final String BASENAME = "base.db"; 
	public static final String SDCard = "SD";
	public static final String Internal = "INTERNAL"; 
	
	public static final String RunPhpLocation = "http://app.gehaxelt.in/ytdownloader/run.php?url="; 
	//http://app.gehaxelt.in/ytdownloader/run.php?url=
	//http://192.168.2.36/run.php?url=
	/**
	 * INFO Variables (LinkMaker)
	 */
	public static String GET_FORMATS = "get_formats"; 
	public static String IS_VALID = "valid_check"; 
	public static String GET_TITLE = "get_title"; 
	public static String GET_THUMBNAIL = "get_thumbnail"; 
	public static String GET_LINK = ""; 
	public static String FORMAT = "format"; 
}

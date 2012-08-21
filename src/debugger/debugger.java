package debugger;

import android.util.Log;

public class debugger {
	public static <E> void debug(E str)
	{
		Log.i("debugger",String.valueOf(str)); 
	}
	public static <E> void debug(E[] str)
	{
		Log.i("debugger",String.valueOf("----------------------New-----------------------")); 
		for (E s: str)
		{
			Log.i("debugger",String.valueOf(s)); 
		}
	
		Log.i("debugger",String.valueOf("----------------------New Ende-----------------------")); 
	}
	public static <E> void err(E str)
	{
		Log.e("debugger_error",String.valueOf(str)); 
	}
}

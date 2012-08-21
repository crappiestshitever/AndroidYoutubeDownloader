package yt.video.download;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import debugger.ShowMessage;
import debugger.debugger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */
public class VideoListViewer extends ExpandableListActivity {

    ExpandableListAdapter mAdapter;
	private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.bundle = bundle;
        // Set up our adapter
        mAdapter = new MyExpandableListAdapter();
        setListAdapter(mAdapter);
        registerForContextMenu(getExpandableListView());
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	
    	debugger.debug(v.getId());
    	ExpandableListView view = (ExpandableListView)findViewById(v.getId());
    	ExpandableListContextMenuInfo info =
	         (ExpandableListContextMenuInfo) menuInfo;
    	
	    String selectedWord = ((TextView) info.targetView).getText().toString();
	    long selectedWordId = (long) info.id;

	    menu.setHeaderTitle(selectedWord);
	    if (selectedWord.equals("SD Storage") || selectedWord.equals("Internal Storage"))
	    {
	    	
	    }
	    else 
	    {
	        menu.add(0, 0, 0, R.string.play);
	        menu.add(0, 1, 0, R.string.delete);
	        menu.add(0, 2, 0, R.string.cancel);
	    }
	
    	
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();

        String title = ((TextView) info.targetView).getText().toString();
        
        debugger.debug("title="+title);
        
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
            Toast.makeText(this, title + ": Child " + childPos + " clicked in group " + groupPos,
                    Toast.LENGTH_SHORT).show();
           
            Execute(item.getItemId(), title, groupPos);
            
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
            //Toast.makeText(this, title + ": Group " + groupPos + " clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
    public void Execute(int MenuID, String FileName, int GroupPos)
    {
        if (GroupPos == 0) //SDCard
        {
        	File ExternalStorage = Environment.getExternalStorageDirectory();
        	final File f = new File(ExternalStorage, FileName);
        	if (f.exists())
        	{
        		switch (MenuID){
        		
	        		case 0: {
	        			//Play Video
	        			VideoPlay play = new VideoPlay(f.getAbsolutePath(), this);
	        			play.playVid();
	        			
	        		}
	        		case 1: 
	        		{
	        			//delete Video
	        			final Builder alert = new Builder(this.getApplicationContext());
	        			alert.setTitle("Warning");
	        			//alert.setIcon(R.drawable.logo_small);
	        			alert.setMessage("Would your really like to delete this video? (It would be also deleted from the drive) ");
	        			alert.setPositiveButton("YES",
	        			 new DialogInterface.OnClickListener() {
	        			  public void onClick(DialogInterface dialog, int id) {
	        				 
	        				  if (!f.delete())
	        				  {
	        					  f.deleteOnExit();
	        				  }
	        			  }});
	        			alert.setNegativeButton("NO",
	        			 new DialogInterface.OnClickListener() {
	        			  public void onClick(DialogInterface dialog, int id) {
	        				  //do_nothing
	        			  }
	        			 });
	        			Handler handler = new Handler(Looper.getMainLooper());
	        			handler.post(new Runnable() {
	        				
	        				@Override
	        				public void run() {
	        					// TODO Auto-generated method stub
	        					alert.show(); 
	        				}
	        			});
	        		}
	        		default: 
	        		{
	        			//do nothing
	        		}
        		}
        	}
        	else {
        		//File does not exist
	        		ShowMessage show = new ShowMessage(this);
	        		show.show("Do you wanna remove the File from Database ?");
        		}
        	
        }
        else // intern
        {
        	File f = new File(FileName); 
        	
        	if (f.exists()) //File Availiable
        	{
        		
        	}
        	else {
        		//File does not exist
	        		ShowMessage show = new ShowMessage(this);
	        		show.show("Do you wanna remove the File from Database ?");
        		}
        }
    }
    /**
     * A simple adapter which maintains an ArrayList of photo resource Ids. 
     * Each photo is displayed as an image. This adapter supports clearing the
     * list of photos and adding a new photo.
     *
     */
    public class MyExpandableListAdapter extends BaseExpandableListAdapter {
        // Sample data set.  children[i] contains the children (String[]) for groups[i].
    	
    	String[] SDList = bundle.getStringArray("SD");
    	String[] InternalList = bundle.getStringArray("Internal");
 
    	private String[][] children = { SDList, InternalList};
    	private String[] groups = {"SD Storage","Internal Storage"};
    	public MyExpandableListAdapter()
    	{
    		if (SDList==null && InternalList == null)
    		{
    			children = new String[2][0];
    		}
    		else if (SDList==null)
    		{
    			children = new String[2][SDList.length];
    			for (int i=0;i<SDList.length;i++)
    				children[0][i] = SDList[i];
    		}
    		else if (InternalList == null)
    		{
    			children = new String[2][InternalList.length];
    			for (int i=0;i<InternalList.length;i++)
    				children[0][i] = InternalList[i];
    		}
    			
    	}
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 64);

            TextView textView = new TextView(VideoListViewer.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setTextSize((float) 16.0);
            textView.setBackgroundColor(Color.BLACK);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
            return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setTextSize((float) 24.0);
            textView.setBackgroundColor(Color.BLACK);
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }
}
package com.tabexample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.provider.Browser;

import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;

public class Tab2Activity  extends Activity
{
		private static final String TAG = "UDL_tab2_Activity";
		Button clearDataButton;
		Button expoorDataButton;
		TextView exportDataDir;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);   
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            setContentView(R.layout.activity_tab2);
            
            File CreateActivityMonitor = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");

            if(!CreateActivityMonitor.exists() && !CreateActivityMonitor.isDirectory()) 
            {
                // create empty directory
                if (CreateActivityMonitor.mkdirs())
                {
                    Log.i("CreateDir","App dir created");
                }
                else
                {
                    Log.w("CreateDir","Unable to create app dir!");
                }
            }
            else
            {
                Log.i("CreateDir","App dir already exists");
            }
            
            File CreateMDLSetting =   new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/setting");
            if(!CreateMDLSetting.exists() && !CreateMDLSetting.isDirectory()) 
            {
                // create empty directory
                if (CreateMDLSetting.mkdirs())
                {
                    Log.i("CreateDir","App dir created");
                }
                else
                {
                    Log.w("CreateDir","Unable to create app dir!");
                }
            }
            else
            {
                Log.i("CreateDir","App dir already exists");
            }
            File CreateMDLData = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/data/");
            if(!CreateMDLData.exists() && !CreateMDLData.isDirectory()) 
            {
                // create empty directory
                if (CreateMDLData.mkdirs())
                {
                    Log.i("CreateDir","App dir created");
                }
                else
                {
                    Log.w("CreateDir","Unable to create app dir!");
                }
            }
            else
            {
                Log.i("CreateDir","App dir already exists");
            }
            
            
            clearDataButton=(Button)findViewById(R.id.button_clear_data);
            
            clearDataButton.setOnClickListener(new View.OnClickListener() {
                
                public void onClick(View v)
                {

                	
                	Log.v(TAG, "clear data");
                	Toast.makeText(getBaseContext(), "Clear all data", Toast.LENGTH_SHORT).show();

                	
          		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/");
         		   File file = new File(rootDirect,"RecordStartMarker.txt");

         		    if(!file.exists()) //clear when not during recording
                	
                	{
	        			File subjectDirect_activity = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
	        			File file_activity = new File(subjectDirect_activity,"activityLog.txt");
	        			file_activity.delete();
	        			
	        			File subjectDirect_activity_raw = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
	        			File file_activity_raw = new File(subjectDirect_activity_raw,"activityLogRaw.txt");
	        			file_activity_raw.delete();
	                	
	                	File subjectDirect_touch = new File(Environment.getExternalStorageDirectory() + "/");
	        			File file_touch = new File(subjectDirect_touch,"touchRecord.txt");
	        			file_touch.delete();	
	        			
	        			File subjectDirect_touch_raw = new File(Environment.getExternalStorageDirectory() + "/");
	        			File file_touch_raw = new File(subjectDirect_touch_raw,"touchRecordRaw.txt");
	        			file_touch_raw.delete();
	        			
	        			File subjectDirect_url_raw = new File(Environment.getExternalStorageDirectory() + "/");
	        			File file_url_raw = new File(subjectDirect_url_raw,"urlDataRaw.txt");
	        			file_touch_raw.delete();
	                	
	                	File subjectDirect_keyboard = new File(Environment.getExternalStorageDirectory() + "/uKeyboardLogger/"+"keyboardRecord");
	        			File file_keyboard = new File(subjectDirect_keyboard,"KeyEvent-keyboardRecord.txt");
	        			file_keyboard.delete();                              	
                	}
                	else 
                	{
                		Toast.makeText(getBaseContext(), "Can't clear during recording", Toast.LENGTH_SHORT).show();
                	}
                }
            });
                  
            
            expoorDataButton=(Button)findViewById(R.id.buttonSaveData);
            
            expoorDataButton.setOnClickListener(new View.OnClickListener() {
                
                public void onClick(View v)
                {
                	Log.v(TAG, "exportData");
                	
            		String DestFolderName = "DataLog-"+ new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
        					
                	
        			File dataDestDirect = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/data/"+DestFolderName);
        			dataDestDirect.mkdir() ;
        			
        			File activitySrcDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
        			File keyboardSrcDirect = new File(Environment.getExternalStorageDirectory() + "/uKeyboardLogger/keyboardRecord");
        			File touchSrcDirect = new File(Environment.getExternalStorageDirectory() + "/");
        			File urlSrcDirect = new File(Environment.getExternalStorageDirectory() + "/");
        			
        			File activitySrcFile = new File(activitySrcDirect,"activityLogRaw.txt");
        			File keyboardSrcFile = new File(keyboardSrcDirect,"KeyEvent-keyboardRecord.txt");
        			File touchSrcFile = new File(touchSrcDirect,"touchRecordRaw.txt");
        			File urlSrcFile = new File(touchSrcDirect,"urlDataRaw.txt");
        			
        			File activityDestFile = new File(dataDestDirect,"activityLogRaw.txt");
        			File keyboardDestile = new File(dataDestDirect,"KeyEvent-keyboardRecord.txt");
        			File touchdDestFile = new File(dataDestDirect,"touchRecordRaw.txt");
        			File urlDestFile = new File(dataDestDirect,"urlDataRaw.txt");
        			       			       			
                	//Copy Activity
        			try {
        				copy(activitySrcFile, activityDestFile);
        			}
    				catch (IOException e) {
    					e.printStackTrace();
    				}
                	//Copy keyboard
        			try {
        				copy(keyboardSrcFile, keyboardDestile);
        			}
    				catch (IOException e) {
    					e.printStackTrace();
    				}               	
                	//Copy touch
        			try {
        				copy(touchSrcFile, touchdDestFile);
        			}
    				catch (IOException e) {
    					e.printStackTrace();
    				}       			
                	//Copy url
        			try {
        				copy(urlSrcFile, urlDestFile);
        			}
    				catch (IOException e) {
    					e.printStackTrace();
    				}
                	
        			//Toast.makeText(getApplicationContext(), "Export raw data successfully!", Toast.LENGTH_SHORT).show();
                
        			//exportDataDir = (TextView) findViewById(R.id.textViewDir);
        			//exportDataDir.setText("Data is saved at: "+"/sdcard/MobileDataLogger/"+ DestFolderName);
        			String toast_str = "Data is saved at: "+"/sdcard/MobileDataLogger/"+ DestFolderName; 
        			Toast.makeText(getBaseContext(), toast_str, Toast.LENGTH_LONG).show();
                }
            });
        }  
        
        
        
        
        @Override
        public void onBackPressed() {
            // your code.
        	Log.d(TAG, "onBackPressed");
        }
        
        //Move Everything from OnCreate to OnResume so it refreshes everytime
        @Override
        public void onResume() {
            super.onResume();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            //Log.d(TAG, "onResume");
            setContentView(R.layout.activity_tab2);
            
            
            TableLayout tl = (TableLayout) findViewById(R.id.main_table);
    		
            //List of content to be displayed in the table
            List<String> appNameList = new ArrayList<String>();
            List<Drawable> appIconList = new ArrayList<Drawable>();
            List<Long> appTimeList = new ArrayList<Long>();
            List<Integer> appTouchCnt = new ArrayList<Integer>();
            List<Integer> appKeyStrokeCnt = new ArrayList<Integer>();
            List<Integer> appUrlCnt = new ArrayList<Integer>();
            
            FileOperations fop_runtime_rawdata = new FileOperations();
            
            //================================================================================
            //The code below calculate the app run time and includes the app names and icons
            //================================================================================
            FileOperations fop_runtime = new FileOperations(); // for reading activity log
            if( ActivityFileExist())
            {	
	            String text = fop_runtime.read("ActivityMonitor/activityLog");            
	            String[] split_lines_runtime = text.split("\n");
	            
	            String startTime = "0"; 
	            String stopTime = "0";
	            String tempAppName= "";
	            String name = "";
	            
	            // The logic below will produce the app name, app start and stop time
	            for(int i=0; i < split_lines_runtime.length; i++)
	            {
	                String[] split_brace = split_lines_runtime[i].split("[{]");
	                String[] split_double_space = split_brace[0].split("[	]");
	                String[] split_slash = split_brace[1].split("[/]");
	                //Log.v(TAG, split_slash[0]);//this contains the package name
	                String package_name = split_slash[0];
	                //Log.v(TAG, split_double_space[0]); //this contains the time stamp
	                
	                if(!package_name.equals("com.tabexample") && !package_name.equals("com.sec.android.app.launcher"))
	                {               	
	                	if(!package_name.equals(tempAppName))
	                	{
	                		startTime = split_double_space[0];
	                		

	                		
	                		
	                		//Log.d(TAG, package_name);
	                		
	                        //The code below convert the package name to app name... 
							try 
							{
									Log.d(TAG, package_name);
									ApplicationInfo app = this.getPackageManager().getApplicationInfo(package_name, 0);							
			                			                        PackageManager p = this.getPackageManager();
			                        name = p.getApplicationLabel(app).toString();	//access name                        
			                        Drawable icon = p.getApplicationIcon(app);	          //access icon              
			                        	                        
			                        appNameList.add(name);
			                        appIconList.add(icon);			                        

									String outline = startTime + "\t"
									+new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date(Long.parseLong(startTime)))+ "\t"
									+ name + "\t" + "start";
									fop_runtime_rawdata.write("/ActivityMonitor/activityLogRaw", outline+ "\n");
			              			                				                        									
							} catch (NameNotFoundException e) 
							{
									// TODO Auto-generated catch block
									e.printStackTrace();
									//Log.d(TAG, "Caught Exception!!");
							}                                         		
	                		//Log.d(TAG, startTime);
	                		
	                		tempAppName = package_name; //update the temp App Name              		
	                	}               	             
	                }
	                //The if statement below calculate the stop time
	            	if(package_name.equals("com.sec.android.app.launcher"))
	            	//This is true because we must launch UDL to see the data so the last app must proceeds the launcher function
	            	{ 
		                    String[] split_brace_prev = split_lines_runtime[i-1].split("[{]");
		                    String[]   split_double_space_prev = split_brace_prev[0].split("[	]");
		                    String[] split_slash_prev = split_brace_prev[1].split("[/]");
	
		                    String package_name_prev = split_slash_prev[0];
		                    
		                    if(package_name_prev.equals(tempAppName))
		                    {
		                    	//Log.d(TAG, "StopTime: " + split_double_space_prev[0]); //this contains the time stamp
		                    	stopTime = split_double_space_prev[0];
		                    	
		                		String outline = stopTime +"\t"
		                				+new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date(Long.parseLong(stopTime)))+ "\t"
		                				+ name + "\t" +  "stop";
		                			                		
		                		fop_runtime_rawdata.write("/ActivityMonitor/activityLogRaw", outline+ "\n");
		                    	
		                    	
		                    	//Log.d(TAG, "StartTime: " + startTime);
		                    	appTimeList.add(Long.parseLong(stopTime) - Long.parseLong(startTime));
		                    	//Log.d(TAG, "RunTime:" + Long.toString(Long.parseLong(stopTime) - Long.parseLong(startTime) ));  
		                    	
		                    	//============================================================================= 
		                    	//The code below read the touch log file and calculate the touch count
		                    	//============================================================================= 
		                    	 FileOperations fop_touch = new FileOperations(); // for reading activity log
		                    	 if( TouchFileExist())
		                    	 {
			                         String text_touch = fop_touch.read("touchRecord");
			                         
			                         String[] split_lines_touch = text_touch.split("\n"); 
			                         
			                         Integer touch_cnt = 0; 
			                       
			                         for(int j=0; j < split_lines_touch.length; j++)
			                         {               	 
			                    	   //Log.v(TAG, "the comparison " + Long.parseLong(stopTime) +" "+ Long.parseLong(split_lines_touch[j]) );
			                    	   	if((Long.parseLong(startTime) - Long.parseLong(split_lines_touch[j].split("\t")[0])) < 0 && (Long.parseLong(stopTime) - Long.parseLong(split_lines_touch[j].split("\t")[0])) > 0)
			                        	 {
			                        		 touch_cnt++;
			                        		 //Log.d(TAG, "touchIncrement");
			                        		 
			                        	 } 
			                         }           	 
			                         appTouchCnt.add(touch_cnt); 
		                    	 }
		                    	 else 
		                    	 {
		                    		 appTouchCnt.add(0);
		                    	 }
		                       //=============================================================================  
		                       //The code below read the keystroke log file and calculate the keystroke count
		                       //=============================================================================
		                    	 FileOperations fop_keystroke = new FileOperations(); // for reading activity log
		                    	 
		                    	 if(keyBoardFileExist())
		                    	 {	 
			                         String text_keystroke = fop_keystroke.read("uKeyboardLogger/keyboardRecord/KeyEvent-keyboardRecord");
			                         String[] split_lines_keystroke = text_keystroke.split("\n");
			                         
			                         Integer keystroke_cnt = 0; 
			                         
			                         for(int k=0; k < split_lines_keystroke.length; k++)
			                         {                	 
			                    	   //Log.v(TAG, "the comparison " + Long.parseLong(stopTime) +" "+ Long.parseLong(split_lines_touch[j]) );
			                    	   	if((Long.parseLong(startTime) - Long.parseLong(split_lines_keystroke[k].split("\t")[0])) < 0 && (Long.parseLong(stopTime) - Long.parseLong(split_lines_keystroke[k].split("\t")[0])) > 0)
			                        	 {
			                    	   		keystroke_cnt++;
			                        		 
			                        	 } 
			                         } 
			                         appKeyStrokeCnt.add(keystroke_cnt); 
		                    	 }
		                    	 else 
		                    	 {
		                    		 appKeyStrokeCnt.add(0);  
		                    	 }
		                         //=======================================================================================================================
		                         //The code below fetches the chrome data...(Note the code below only works when chrome is open)  
		                         //=======================================================================================================================
		                    	 	  
		                    	 	String[] proj = new String[] { Browser.BookmarkColumns.TITLE,Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE };
		                             Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
		                             String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
		                             Cursor mCur = getContentResolver().query(uriCustom, proj, sel, null, null);
		                             Integer url_cnt = 0;
		                             try {
		                 	            	  mCur.moveToFirst();
		                 		              @SuppressWarnings("unused")
		                 		              String title = "";
		                 		              @SuppressWarnings("unused")
		                 		              String url = "";            
		                 		              @SuppressWarnings("unused")
		                 		              String date = "";
		                 		  
		                 		              if (mCur.moveToFirst() && mCur.getCount() > 0) {
		                 		                  boolean cont = true;
		                 		                  while (mCur.isAfterLast() == false && cont) {
		                 		                  	                                	
		                 		                      title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
		                 		                      Log.d(TAG, "title is: "+ title);
		                 		                      url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
		                 		                      Log.d(TAG, "url is: "+ url);                 
		                 		                      date = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));
		                 		                      Log.d(TAG, "date is: "+ date);                                      
		                 		                      mCur.moveToNext();
		                 		                      
		       
		                 		                      
		                 		                     
		                 		                      
		      			                    	   	if((Long.parseLong(startTime) - Long.parseLong(date) < 0) && (Long.parseLong(stopTime) - Long.parseLong(date) > 0))
		      			                    	   	{
		      			                    	   		url_cnt++;		   	                        		 
		      			                    	   	}     
		                 		                  }
		                 		                 appUrlCnt.add(url_cnt);
		                 		              }
		                             } 
		                             catch (NullPointerException e) {
		                 	            //Log.d(TAG, "Null Pointer!!!Need to run Chrome in the background");
		                 	            Toast.makeText(getApplicationContext(), "Can't access URL. Need to run Chrome in the background",
		                 	            		   Toast.LENGTH_LONG).show();
		                             } 
		                    	 
		                    	 
		                    	 
		                    }		                                        
	            	}    
	           } 	          
            }
            
            
            //===================================================================================
            //This code below is specifically for write the browser raw data
            //===================================================================================
            FileOperations fop_url_raw = new FileOperations();  
    	 	String[] proj = new String[] { Browser.BookmarkColumns.TITLE,Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE };
             Uri uriCustom = Uri.parse("content://com.android.chrome.browser/bookmarks");
             String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
             Cursor mCur = getContentResolver().query(uriCustom, proj, sel, null, null);
             try {
 	            	  mCur.moveToFirst();
 		              @SuppressWarnings("unused")
 		              String title = "";
 		              @SuppressWarnings("unused")
 		              String url = "";            
 		              @SuppressWarnings("unused")
 		              String date = "";
 		  
 		              if (mCur.moveToFirst() && mCur.getCount() > 0) {
 		                  boolean cont = true;
 		                  while (mCur.isAfterLast() == false && cont) {
 		                  	                                	
 		                      title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));	                      
 		                      url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));		                                       
 		                      date = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.DATE));		                                                          
 		                      mCur.moveToNext();
		                      String outline_url = date +"\t"
 		                      + new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date(Long.parseLong(date))) + "\t"
 		                      + title;
		                      
 		                      fop_url_raw.write("urlDataRaw", outline_url+ "\n");
   
 		                  }
 		                
 		              }
             } 
             catch (NullPointerException e) {
 	            //Log.d(TAG, "Null Pointer!!!Need to run Chrome in the background");
 	            Toast.makeText(getApplicationContext(), "Can't access URL. Need to run Chrome in the background",
 	            		   Toast.LENGTH_LONG).show();
             }
            
            
          
            
            //==================================================================================================
            // The code below display the read data 
            //==================================================================================================                                 
            //Find the smallest size among appName and appTouch to prevent out of range crash
             
            
            //Log.e("Test", "appNameList.size() = " + appNameList.size());
            //Log.e("Test", "appTouchCnt.size() = " + appTouchCnt.size()); 
            Integer listCnt_smallest =  appNameList.size();
            if (appNameList.size() > appTouchCnt.size())
            {
            	listCnt_smallest = appTouchCnt.size(); 
            }
             
             
            Integer rcount = 0;            
            int idx = 0;
            while (rcount < listCnt_smallest) 
            {
	            // Create the table row
	            TableRow tr = new TableRow(this);
	            if(rcount%2!=0) tr.setBackgroundColor(Color.GRAY);
	            if(rcount%2==0) tr.setBackgroundColor(Color.LTGRAY);
	            tr.setId(100+rcount);
	            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	
	            ImageView label_appicon_1 = new ImageView(this);
	            label_appicon_1.setId(20+idx);// define id that must be unique
	            //label_appicon_1.setImageResource(R.drawable.ic_launcher);
	            
	            label_appicon_1.setImageResource(R.drawable.ic_launcher);
	            
	            //label_appicon.setScaleType(ScaleType.CENTER_CROP);
	            //label_appicon.setScaleX(0.5f);
	            
	            label_appicon_1.setImageDrawable(appIconList.get(rcount));
	            
	            
	            Drawable drawing = label_appicon_1.getDrawable();
	            if (drawing == null) {
	                return; // Checking for null & return, as suggested in comments
	            }
	            Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

	            // Get current dimensions AND the desired bounding box
	            int width = bitmap.getWidth();
	            int height = bitmap.getHeight();
	            int bounding = dpToPx(130);
	            Log.i("Test", "original width = " + Integer.toString(width));
	            Log.i("Test", "original height = " + Integer.toString(height));
	            Log.i("Test", "bounding = " + Integer.toString(bounding));

	            // Determine how much to scale: the dimension requiring less scaling is
	            // closer to the its side. This way the image always stays inside your
	            // bounding box AND either x/y axis touches it.  
	            float xScale = ((float) bounding) / width;
	            float yScale = ((float) bounding) / height;
	            float scale = (xScale <= yScale) ? xScale : yScale;
	            Log.i("Test", "xScale = " + Float.toString(xScale));
	            Log.i("Test", "yScale = " + Float.toString(yScale));
	            Log.i("Test", "scale = " + Float.toString(scale));

	            // Create a matrix for the scaling and add the scaling data
	            Matrix matrix = new Matrix();
	            matrix.postScale(scale, scale);

	            // Create a new bitmap and convert it to a format understood by the ImageView 
	            Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	            width = scaledBitmap.getWidth(); // re-use
	            height = scaledBitmap.getHeight(); // re-use
	            BitmapDrawable result = new BitmapDrawable(scaledBitmap);
	            Log.i("Test", "scaled width = " + Integer.toString(width));
	            Log.i("Test", "scaled height = " + Integer.toString(height));
	            
	            
	            // Apply the scaled bitmap
	            label_appicon_1.setImageDrawable(result);
	            
	            tr.addView(label_appicon_1); // add the column to the table row here
	            
	            idx++;
	            
	            //Log.d(TAG, "appNameListSize: " + Integer.toString(appNameList.size()));
	            
	            
	            
	            
	    		TextView label_appname_1 = new TextView(this);
	    		label_appname_1.setId(20+idx);
	    		label_appname_1.setText(appNameList.get(rcount));
	    		label_appname_1.setTextColor(Color.WHITE);
	    		label_appname_1.setPadding(0, 50, 10, 5);
	    		label_appname_1.setGravity(Gravity.CENTER_HORIZONTAL);
	    		label_appname_1.setWidth(450);
	    		
	    		label_appname_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
	    		tr.addView(label_appname_1);// add the column to the table row here
	    		
	    		idx++;
	
	            TextView label_touch_cnt_1 = new TextView(this);
	            label_touch_cnt_1.setId(20+idx);// define id that must be unique
	            //label_touch_cnt_1.setText(Integer.toString(appTouchCnt.get(rcount))); // set the text for the header 
	            //Log.d(TAG, "touchCountSize: " + Integer.toString(appTouchCnt.size()));	
	            if(TouchSettingFileExist())//touch display disabled
	            {
	            	label_touch_cnt_1.setText("X");
	            }
	            else
	            {
	            	label_touch_cnt_1.setText(Integer.toString(appTouchCnt.get(rcount))); // set the text for the header 
	            }            
	            label_touch_cnt_1.setTextColor(Color.WHITE); // set the color
	            //label_touch_cnt.setGravity(Gravity.CENTER_HORIZONTAL);
	            label_touch_cnt_1.setPadding(0, 50, 20, 5); // set the padding (if required)
	            label_touch_cnt_1.setWidth(120);
	            tr.addView(label_touch_cnt_1); // add the column to the table row here
	            
	            idx++;
	            
	            TextView label_url_visits_1 = new TextView(this);
	            label_url_visits_1.setId(20+idx);// define id that must be unique
	            if(UrlSettingFileExist())//URL display disabled
	            {
	            	label_url_visits_1.setText("X");
	            }
	            else
	            {
	            	
	            	//Handle Exception when Chrome is not open
                    try {
                    	label_url_visits_1.setText(Integer.toString(appUrlCnt.get(rcount))); // set the text for the header 
                    }
                    catch (RuntimeException e) {
                    	label_url_visits_1.setText("NA");
                    }
                    
                    
	            }
	            
	            label_url_visits_1.setTextColor(Color.WHITE); // set the color
	            label_url_visits_1.setPadding(0, 50, 20, 5); // set the padding (if required)
	            label_url_visits_1.setWidth(100);
	            tr.addView(label_url_visits_1); // add the column to the table row here
	            
	            idx++;
	            
	            
	            //Log.d(TAG, "appKeyStrokeCnt: " + Integer.toString(appKeyStrokeCnt.size()));
	            
	            TextView label_keystroke_1 = new TextView(this);
	            label_keystroke_1.setId(20+idx);// define id that must be unique
	            
	            if(KeyboardSettingFileExist())//KEYBOARD display disabled or keyboard file doesn't exist
	            {
	            	label_keystroke_1.setText("X");
	            }
	            else
	            {
	            	label_keystroke_1.setText(Integer.toString(appKeyStrokeCnt.get(rcount))); // set the text for the header 
	            }
	            label_keystroke_1.setTextColor(Color.WHITE); // set the color
	            label_keystroke_1.setPadding(0, 50, 50, 5); // set the padding (if required)
	            label_keystroke_1.setWidth(100);
	            tr.addView(label_keystroke_1); // add the column to the table row here
	            
	            idx++;
	            
	            
	           
	            
	            TextView label_runtime_1 = new TextView(this);
	            label_runtime_1.setId(20+idx);// define id that must be unique
	            
	            //Log.d(TAG, "appTimeList: " + Integer.toString(appTimeList.size()));
	            
	            if(RuntimeSettingFileExist())//RUNTIME display disabled
	            {
	            	label_runtime_1.setText("X");
	            }
	            else
	            {
	            	label_runtime_1.setText(Long.toString(appTimeList.get(rcount)) + "ms"); // set the text for the header 
	            }
	            
	            label_runtime_1.setTextColor(Color.WHITE); // set the color
	            label_runtime_1.setPadding(0, 50, 5, 5); // set the padding (if required)
	            label_runtime_1.setWidth(130);
	            label_runtime_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
	            tr.addView(label_runtime_1); // add the column to the table row here
	            
	            idx++;
	            
	            // finally add this to the table row
	            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	            rcount++;	                      
            }
            
            
        }
        
        private int dpToPx(int dp)
        {
            float density = getApplicationContext().getResources().getDisplayMetrics().density;
            return Math.round((float)dp * density);
        }
        
    	public boolean keyBoardFileExist() {
 		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/uKeyboardLogger/keyboardRecord");
 		   File file = new File(rootDirect,"KeyEvent-keyboardRecord.txt");
 		   return file.exists();
    	}
    	
    	public boolean ActivityFileExist() {
  		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
  		   File file = new File(rootDirect,"activityLog.txt");
  		   return file.exists();
     	}
        
    	public boolean TouchFileExist() {
   		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/");
   		   File file = new File(rootDirect,"touchRecord.txt");
   		   return file.exists();
      	}
        
       	public boolean TouchSettingFileExist() {
    		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/setting");
    		   File file = new File(rootDirect,"touchDisable.txt");
    		   return file.exists();
       	}
       	
       	public boolean KeyboardSettingFileExist() {
 		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/setting");
 		   File file = new File(rootDirect,"keyboardDisable.txt");
 		   return file.exists();
    	}
       	public boolean UrlSettingFileExist() {
 		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/setting");
 		   File file = new File(rootDirect,"urlDisable.txt");
 		   return file.exists();
    	}
      	public boolean RuntimeSettingFileExist() {
 		   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/MobildDataLogger/setting");
 		   File file = new File(rootDirect,"runtimeDisable.txt");
 		   return file.exists();
    	}
       	
		public void copy(File src, File dst) throws IOException {
		    FileInputStream inStream = new FileInputStream(src);
		    FileOutputStream outStream = new FileOutputStream(dst);
		    FileChannel inChannel = inStream.getChannel();
		    FileChannel outChannel = outStream.getChannel();
		    inChannel.transferTo(0, inChannel.size(), outChannel);
		    inStream.close();
		    outStream.close();
		}
}

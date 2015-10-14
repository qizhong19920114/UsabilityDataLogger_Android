package com.tabexample;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tabexample.FileOperations;
import com.tabexample.Events;
import com.tabexample.Events.InputDevice;
import com.tabexample.MainActivity;
import com.tabexample.StoppableRunnable;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tab1Activity  extends Activity
{
		ActivityManager am;
		ComponentName cn;
		List<RunningTaskInfo> activitylist;
		String LogFilename;
		boolean TokenFileExistence;
		boolean stopped = true;
		
		private Handler mHandler = new Handler();
		private NotificationManager mNotificationManager;
		private int notificationID = 100;
		
		Button startButton;
		private static final String TAG = "UDL_tab1_Activity";
		
		boolean m_bMonitorOn = false; 
		
		Events events = new Events();
				
		@Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);   
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            setContentView(R.layout.activity_tab1);

            //Launch browser...
   //         Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
     //       startActivity(launchIntent);
            startService(new Intent( Tab1Activity.this,StartBrowserService.class));
                     
            startButton = (Button) findViewById(R.id.buttonStart);
            startButton.setTag(1);
            startButton.setText("Start");
            startButton.setOnClickListener( new View.OnClickListener() {
	            @Override
	            public void onClick (View v) {
	            	
	            	
	            	final int status =(Integer) v.getTag();
	            	
		            if(status == 1) //start monitoring; don't get confused by the setTest "Stop"
		            {		                
		            	startButton.setText("Stop");
		                v.setTag(0); //pause
		               
		                int res = events.Init();
		                
		                
						Toast.makeText(getApplicationContext(), "Start Recording.", Toast.LENGTH_SHORT).show();
						
						StartTouchMonitor(); 
						StartActivityMonitor(); 
						StartKeyboardMonitor();
						RecordStartMarker();//use this to prevent data clear during recording
		            } 
		            else //stop monitoring
		            {
		            	startButton.setText("Start");
		                v.setTag(1); //pause
		                
		                Toast.makeText(getApplicationContext(), "Stop Recording.", Toast.LENGTH_SHORT).show();
		                
		                StopTouchMonitor();	
		                StopActivityMonitor(); 
		                StopKeyboardMonitor();
		                RecordStopMarker(); //use this to prevent data clear during recording
		            } 
	            }
            });
                          
            
            
            
        }
		
	    public void onDestroy() {
	    	super.onDestroy();
	    	Log.d(TAG, "App destroyed.");
	    	StopTouchMonitor();
	    	events.Release();
	    }
		
        @Override
        public void onBackPressed() {
            // your code.
        	Log.d(TAG, "onBackPressed");
        }
		
		public void StopTouchMonitor() {
			m_bMonitorOn = false; //stop reading thread
		}
		
		public void StartTouchMonitor() {
			m_bMonitorOn = true;
			
			//This for loop open the touch screen device
			for (InputDevice idev:events.m_Devs) {
	        	if (idev.getId() == 21) {
	        		
	        		Log.d(TAG, "The device name is: " + idev.getPath());
	        			        		
	        		if (idev.Open(true)) {	        			
	        			// inform user
	        			Toast.makeText(this, "Device opened successfully!", Toast.LENGTH_SHORT).show();
	        			
	        			if(idev.getOpen())
	        			{	
		        			String name = events.m_Devs.get(21).getName();
		        			Log.d(TAG, "spinner selected: 0"+ " Name:"+name);
		        			Toast.makeText(this, "New device selected:"+name, Toast.LENGTH_SHORT).show();
	        			}
	        			
	        		} else {
	        			Toast.makeText(this, "Device failed to open. Do you have root?", Toast.LENGTH_SHORT).show();
	        		}
	        		break;
	        	} 
			}
			
			//START TRACKING Touches (It works fine now)
			Thread b = new Thread(new Runnable() {
				public void run() {
					int Xpos = 0 ; 
					int Ypos = 0; 
					int MajorNum = 0; 
					int MinorNum = 0; 
					int TrackID = 0; 
					double TouchArea = 0; 
				    FileOperations fop_rawdata = new FileOperations();
				    String outline_raw;
					while (m_bMonitorOn) {
						for (InputDevice idev:events.m_Devs) {							
							
							if (idev.getOpen() && (0 == idev.getPollingEvent()) && idev.getPath().equals( "/dev/input/event4")) {																
								if ((idev.getSuccessfulPollingCode()==57)&&(idev.getSuccessfulPollingValue()!=-1)){		
									long CurrentKeyTimeStamp = System.currentTimeMillis();
									
									
									Log.d(TAG, "Start tracking with ID: "+idev.getSuccessfulPollingValue());	
									
									Log.d(TAG, "TimeStamp: " + String.valueOf(CurrentKeyTimeStamp));
									TrackID = idev.getSuccessfulPollingValue(); 
									
									
									String outline = String.valueOf(CurrentKeyTimeStamp)+ "\t" 
											+ String.valueOf(TrackID);
									
									outline_raw = String.valueOf(CurrentKeyTimeStamp)+ "\t" 
												+new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date())+ "\t"
												+ "start";
									
								    FileOperations fop = new FileOperations();
								    fop.write("touchRecord", outline+ "\n");
								    fop_rawdata.write("touchRecordRaw", outline_raw+ "\n");
								}
								else if (idev.getSuccessfulPollingCode()==53){
									Log.d(TAG, "X position is: "+ idev.getSuccessfulPollingValue());	
									Xpos = idev.getSuccessfulPollingValue(); 
								}
								else if (idev.getSuccessfulPollingCode()==54){
									
									long CurrentKeyTimeStamp = System.currentTimeMillis();
									Log.d(TAG, "Y position is: "+ idev.getSuccessfulPollingValue());										
									Ypos = idev.getSuccessfulPollingValue(); 
									
									outline_raw = String.valueOf(CurrentKeyTimeStamp)+ "\t" 
											+new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date())+ "\t"
											+  "(" + String.valueOf(Xpos) + "," + String.valueOf(Ypos) + ")" ;
									
									fop_rawdata.write("touchRecordRaw", outline_raw+ "\n");
								}
								else if ((idev.getSuccessfulPollingCode()==57)&&(idev.getSuccessfulPollingValue()==-1)){
									long CurrentKeyTimeStamp = System.currentTimeMillis();
									outline_raw = String.valueOf(CurrentKeyTimeStamp)+ "\t" 
											+new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date())+ "\t"
											+ "stop";
									fop_rawdata.write("touchRecordRaw", outline_raw+ "\n");
									Log.i(TAG, "Stop Tracking");	
								}
							}
						}
					}
				}
			});			
			b.start(); 			
		}
		
		public void StartActivityMonitor() {
			
        	stopped = false;
        	// when click the toggle, delete the log file name
    		deleteFile("logfilename.txt");
    		LogFilename = "activityLog-"
					+ new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date())
					+ ".txt";

        	// notification started.
        	displayNotification();

        	if (m_bMonitorOn == true){
        		createLogFileRecord("logfilename.txt");
        		String logFilenameSaved = ReadLogFileRecord("logfilename.txt");
        		onStartThread(100);   
        		
        	}else{
        		onStopThread();
        		String logFilenameSaved = ReadLogFileRecord("logfilename.txt");       		
        	} 

		}
		
		public void StopActivityMonitor() {      	
        	//notify monitor stopped
        	monitorStopped();
        	Toast.makeText(getBaseContext(), "StopActivityMonitor()", Toast.LENGTH_SHORT).show();
        	onStopThread();
        	
		}
	
		
		public void RecordStartMarker()
		{
			try 
			{				
				File subjectDirect = new File(Environment.getExternalStorageDirectory() + "/");								
				File file = new File(subjectDirect,"RecordStartMarker.txt");
				FileOutputStream fos_mark = new FileOutputStream(file, true);
				String outline=" ";
				fos_mark.write(outline.getBytes());									
				fos_mark.close();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}	
		    catch (IOException e) 
			{
		    	e.printStackTrace();
		    }
		}
		
		public void RecordStopMarker()
		{
			File subjectDirect = new File(Environment.getExternalStorageDirectory() + "/");
			File file = new File(subjectDirect,"RecordStartMarker.txt");
			file.delete();
		}
		
		
		//Create a file to mark the start of keyboard monitoring
		public void StartKeyboardMonitor()
		{			
			try 
			{				
				File subjectDirect = new File(Environment.getExternalStorageDirectory() + "/uKeyboardLogger/"+"keyboardRecord");
				if(!subjectDirect.exists())
				{				   
					subjectDirect.mkdir() ;       	
				}								
				File file = new File(subjectDirect,"startKeyBoardRecord.txt");
				FileOutputStream fos_mark = new FileOutputStream(file, true);
				String outline=" ";
				fos_mark.write(outline.getBytes());									
				fos_mark.close();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}	
		    catch (IOException e) 
			{
		    	e.printStackTrace();
		    }						
		}
		//Delete a file to mark the stop of keyboard monitoring		
		public void StopKeyboardMonitor(){
			File subjectDirect = new File(Environment.getExternalStorageDirectory() + "/uKeyboardLogger/"+"keyboardRecord");
			File file = new File(subjectDirect,"startKeyBoardRecord.txt");
			file.delete();
		}
		
		public void onStopThread() {
		     mTask.stop();       
		     mHandler.removeCallbacks(mTask);
		 }
		 
		private StoppableRunnable mTask = new StoppableRunnable() {
		     public void stoppableRun() {     
		    			        	
		    		Log.d(TAG, "m_bMonitorOn: "+Boolean.toString(m_bMonitorOn));
		    		
		         	if (m_bMonitorOn == true){
						//sample data at 10Hz, or every 100 msec
		         		 SampleActivityOnce();
			             onStartThread(100);  		
			             Log.d(TAG, "onStartThread(100)");
			        }
		         	else{
		         		onStopThread();
		         	}		         							    
		         }
		     };
		
		public boolean isFileExist(String filename) {
				   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
					File file = new File(rootDirect,filename);

				return file.exists();
		}     
		     
		protected void monitorStopped() {
		      Log.i("Start", "notification");

		      /* Invoking the default notification service */
		      NotificationCompat.Builder  mBuilder = 
		      new NotificationCompat.Builder(this);	

		      mBuilder.setContentTitle("Activity Monitor Stopped");
		      mBuilder.setContentText("Activity Monitor has been stopped successfully");
		      
		      /* Creates an explicit intent for an Activity in your app */
		      Intent resultIntent = new Intent(this, MainActivity.class);
		      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		      stackBuilder.addParentStack(MainActivity.class);
		      /* Adds the Intent that starts the Activity to the top of the stack */
		      stackBuilder.addNextIntent(resultIntent);
		      PendingIntent resultPendingIntent =
		         stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		         );
		      mBuilder.setContentIntent(resultPendingIntent);
		      mNotificationManager =
		      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		      /* notificationID allows you to update the notification later on. */
		      mNotificationManager.notify(notificationID, mBuilder.build());
		   }
		
		public void SampleActivityOnce(){
			 am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			 activitylist = am.getRunningTasks(30);				
			 SaveActivityList(LogFilename,activitylist);
			 //Toast.makeText(getBaseContext(), "SampleActivityOnce", Toast.LENGTH_SHORT).show();
			 
		}
				
		public void SaveActivityList(String filename,List<RunningTaskInfo> activityList ) {
			//data is saved to 
			///mnt/sdcard/ActivityMonitor 
			try {
				   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
				   
					 //Toast.makeText(getBaseContext(), String.valueOf(rootDirect), Toast.LENGTH_SHORT).show();

				   if(!rootDirect.exists())
				    {				   rootDirect.mkdir() ;       

				    }
				//File sdcard = Environment.getExternalStorageDirectory();
				File file = new File(rootDirect,"activityLog.txt");
				// Toast.makeText(getBaseContext(), "files created", Toast.LENGTH_SHORT).show();

				FileOutputStream fos2 = new FileOutputStream(file, true);

				long CurrentKeyTimeStamp = System.currentTimeMillis();
				String outline = String.valueOf(CurrentKeyTimeStamp) + "\t";
				if (activityList.size()>1){
				for (int i=0;i<activityList.size();i++){
					outline = outline 
							+String.valueOf(activityList.get(i).topActivity)+ "\t";
					//Log.d(TAG, "TopActivity: " + String.valueOf(activityList.get(i).topActivity));
				}
				
			}
				outline = outline+ "\n";				
				fos2.write(outline.getBytes());
				fos2.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void onStartThread(long delayMillis) {
		     mHandler.postDelayed(mTask, delayMillis);
		 }
		
		protected void displayNotification() {
		      Log.i("Start", "notification");

		      /* Invoking the default notification service */
		      NotificationCompat.Builder  mBuilder = 
		      new NotificationCompat.Builder(this);	

		      mBuilder.setContentTitle("Activity Monitor Starts");
		      mBuilder.setContentText("Activity Monitor has been started successfully");
		      //mBuilder.setTicker("New Message Alert!");

		      /* Increase notification number every time a new notification arrives */
		      //mBuilder.setNumber(++numMessages);
		      
		      /* Creates an explicit intent for an Activity in your app */
		      Intent resultIntent = new Intent(this, MainActivity.class);

		      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		      stackBuilder.addParentStack(MainActivity.class);

		      /* Adds the Intent that starts the Activity to the top of the stack */
		      stackBuilder.addNextIntent(resultIntent);
		      PendingIntent resultPendingIntent =
		         stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		         );

		      mBuilder.setContentIntent(resultPendingIntent);

		      mNotificationManager =
		      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		      /* notificationID allows you to update the notification later on. */
		      mNotificationManager.notify(notificationID, mBuilder.build());
		   }	
		
		public void createTokenFile(String filename) {

				try {

					   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
					   
					   if(!rootDirect.exists())
					    {				   rootDirect.mkdir() ;       

					    }
					   
					//File sdcard = Environment.getExternalStorageDirectory();
					File file = new File(rootDirect,filename);
					// Toast.makeText(getBaseContext(), "files created", Toast.LENGTH_SHORT).show();

					FileOutputStream fos2 = new FileOutputStream(file, true);

					long CurrentKeyTimeStamp = System.currentTimeMillis();
					String outline = String.valueOf(CurrentKeyTimeStamp) + "\t";

					outline = outline+ "\n";
					
					fos2.write(outline.getBytes());
					fos2.close();			

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		public void createLogFileRecord(String filename) {

			try {

				   File rootDirect = new File(Environment.getExternalStorageDirectory() + "/ActivityMonitor");
				   
				   if(!rootDirect.exists())
				    {				   rootDirect.mkdir() ;       

				    }
				   
				//File sdcard = Environment.getExternalStorageDirectory();
				File file = new File(rootDirect,filename);
				// Toast.makeText(getBaseContext(), "files created", Toast.LENGTH_SHORT).show();

				FileOutputStream fos2 = new FileOutputStream(file, true);

				long CurrentKeyTimeStamp = System.currentTimeMillis();
//				String outline = String.valueOf(CurrentKeyTimeStamp) + "\t";

				String outline = LogFilename+ "\n";
				
				fos2.write(outline.getBytes());
				fos2.close();			

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public String ReadLogFileRecord(String filename) {
				//read a text file from android
				//http://stackoverflow.com/questions/2902689/how-can-i-read-a-text-file-from-the-sd-card-in-android
			String logFilenameSaved="";
			//Find the directory for the SD Card using the API
			//*Don't* hardcode "/sdcard"
			//File sdcard = Environment.getExternalStorageDirectory();

			File sdcard = Environment.getExternalStorageDirectory();


			try {
				//Get the text file
				File file = new File(sdcard,"/ActivityMonitor/"+filename);


				//Read text from file
				StringBuilder text = new StringBuilder();
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    logFilenameSaved= br.readLine();
			}
			catch (IOException e) {
			    //You'll need to add proper error handling here
			}
			return logFilenameSaved;
			}

		//This function will be called for export raw data

		
		
}
package com.tabexample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;


public class FileOperations {
    public FileOperations() {

       }

    public Boolean write(String fname, String fcontent){
           try {

               String fpath = "/sdcard/"+fname+".txt";

               File file = new File(fpath);

               // If file does not exists, then create it
               if (!file.exists()) {
                   file.createNewFile();
               }

//               FileWriter fw = new FileWriter(file.getAbsoluteFile());
//               BufferedWriter bw = new BufferedWriter(fw);
//               bw.write(fcontent);
//               bw.close();
               
               FileOutputStream fos2 = new FileOutputStream(file, true);
				fos2.write(fcontent.getBytes());									
				fos2.close();

               Log.d("Suceess","Sucess");
               return true;

           } catch (IOException e) {
               e.printStackTrace();
               return false;
           }

    }
    
 

    @SuppressWarnings("resource")
	public String read(String fname){

        BufferedReader br = null;
        String response = null;

           try {

               StringBuffer output = new StringBuffer();
               String fpath = "/sdcard/"+fname+".txt";

               br = new BufferedReader(new FileReader(fpath));
               String line = "";
               while ((line = br.readLine()) != null) {
                   output.append(line +"\n");
               }
               response = output.toString();

           } catch (IOException e) {
               e.printStackTrace();
               return null;

           }
           return response;

    }
}
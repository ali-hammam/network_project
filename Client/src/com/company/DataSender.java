package com.company;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by HP on 5/12/2022.
 */
public class DataSender {
    public static void postFile(String path, DataOutputStream payload){
        try {
            int bytes = 0;
            File file = new File(path);
            if(file.exists() && !file.isDirectory()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4 * 1024];
                while ((bytes = fileInputStream.read(buffer)) != -1) {
                    payload.write(buffer, 0, bytes);
                    payload.flush();
                }
                fileInputStream.close();
            }else{
                System.out.println("File Not Found!!");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void postImage(String path, DataOutputStream payload){
        try {
            int bytes = 0;
            File file = new File(path);
            if(file.exists() && !file.isDirectory()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4 * 1024];
                while ((bytes = fileInputStream.read(buffer)) != -1) {
                    payload.write(buffer, 0, bytes);
                    payload.flush();
                }
                fileInputStream.close();
            }else{
                System.out.println("File Not Found!!");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

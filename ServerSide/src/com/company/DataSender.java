package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by HP on 5/11/2022.
 */
public class DataSender {
    private boolean GETError = false;
    public void sendFileName(String fileName, DataOutputStream clientWriteSource){
        try {
            int bytes = 0;
            File file = new File("C:\\Users\\HP\\IdeaProjects\\ServerSide\\src\\com\\company\\localStorage\\" + fileName);
            if(file.exists() && !file.isDirectory()) {
                clientWriteSource.writeUTF("HTTP/1.0 200 OK.\r\n");
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4 * 1024];
                while ((bytes = fileInputStream.read(buffer)) != -1) {
                    clientWriteSource.write(buffer, 0, bytes);
                    clientWriteSource.flush();
                }
                fileInputStream.close();
            }else{
                this.GETError = true;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendImage(String fileName, DataOutputStream clientWriteSource){
        String extension = fileName.split("[.]")[1];
        try {
            int bytes = 0;
            File file = new File("C:\\Users\\HP\\IdeaProjects\\ServerSide\\src\\com\\company\\localStorage\\" + fileName);
            if(file.exists() && !file.isDirectory()) {
                clientWriteSource.writeUTF("HTTP/1.0 200 OK.\r\n");
                BufferedImage image = ImageIO.read(file);
                ImageIO.write(image,extension,clientWriteSource);
            }else{
                this.GETError = true;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isGETError(){
        return  GETError;
    }
}

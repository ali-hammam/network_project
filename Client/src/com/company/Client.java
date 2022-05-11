package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by HP on 5/8/2022.
 */
public class Client{
    public Client(String requestMethod, String fileName){
        try {
            String []imageExtensionArray = {"png", "jpg", "jpeg"};
            InetAddress ip = InetAddress.getByName("localhost");
            Socket other = new Socket(ip, 8000);

            DataInputStream otherReadSource = new DataInputStream(other.getInputStream());
            DataOutputStream otherWriteSource = new DataOutputStream(other.getOutputStream());

            switch (requestMethod) {
                case "get": {
                    String fileExtension = fileName.split("[.]")[1].toLowerCase();
                    makeGetRequest(otherWriteSource, fileName, ip.toString());
                    String str = otherReadSource.readUTF();

                    if(str.split(" ")[1].equals("404")){
                        System.out.println(str);
                    }else {
                        System.out.println(str);
                        if (Arrays.asList(imageExtensionArray).contains(fileExtension)) {
                            DataReceiver.receiveImage(fileName, otherReadSource);
                        } else {
                            DataReceiver.receiveFile(fileName, otherReadSource);
                        }
                    }
                    break;
                }

                default:
                    System.out.println("Unsupported Method");
                    break;
            }

            otherReadSource.close();
            otherWriteSource.close();
            other.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeGetRequest(DataOutputStream writeRequest, String fileName, String host){
        try {
            writeRequest.writeUTF("GET /" + fileName +" HTTP/1.0");
            writeRequest.writeUTF("HOST: " + host);
            writeRequest.writeUTF("TERMINATE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

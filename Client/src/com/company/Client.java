package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                    if(checkCache(fileName)) {
                        if (Arrays.asList(imageExtensionArray).contains(fileExtension)) {
                            Cache.receiveImageFromCache(fileName);
                        } else {
                            Cache.receiveFileFromCache(fileName);
                        }
                    }else{
                        makeGetRequest(otherWriteSource, fileName, ip.toString());

                        String str = otherReadSource.readUTF();

                        if (str.split(" ")[1].equals("404")) {
                            System.out.println(str);
                        } else {
                            System.out.println(str);
                            if (Arrays.asList(imageExtensionArray).contains(fileExtension)) {
                                DataReceiver.receiveImage(fileName, otherReadSource);
                            } else {
                                DataReceiver.receiveFile(fileName, otherReadSource);
                            }
                        }
                    }
                    break;
                }
                case "post":{
                    makePostRequest(otherWriteSource, fileName, ip.toString());
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

    public void makePostRequest(DataOutputStream writeRequest, String path, String host){
        try {
            Path filePath = Paths.get(path);
            String fileName = filePath.getFileName().toString();
            writeRequest.writeUTF("POST /" + fileName +" HTTP/1.0");
            writeRequest.writeUTF("HOST: " + host);
            writeRequest.writeUTF("TERMINATE");
            if(isImageExtension(fileName)){
                DataSender.postImage(path, writeRequest);
            }else{
                DataSender.postFile(path, writeRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isImageExtension(String fileName){
        String []imageExtensionArray = {"png", "jpg", "jpeg"};
        String fileExtension = fileName.split("[.]")[1].toLowerCase();
        return Arrays.asList(imageExtensionArray).contains(fileExtension);
    }

    public boolean checkCache(String fileName){
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }
}


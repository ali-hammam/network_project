package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by HP on 5/8/2022.
 */
public class Server {
    public Server(){
        //listen to clients and return socket
        try {

            ServerSocket server = new ServerSocket(8000);
            while(true){
                Socket client = server.accept();

                DataInputStream clientReadSource = new DataInputStream(client.getInputStream()); //it will read the data as string or int or any type as you want
                DataOutputStream clientWriteSource = new DataOutputStream(client.getOutputStream());

                clientWriteSource.writeUTF("Server Handshaked now.");

                String str = "";
                List<String> request = new ArrayList<>();
                while(!str.equals("TERMINATE")) {
                    str = clientReadSource.readUTF();
                    request.add(str);
                }
                processRequestType(request, clientWriteSource);

                clientWriteSource.close();
                clientReadSource.close();
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequestType(List<String> request, DataOutputStream clientWriteSource){
        String requestMethod = "";
        String str = request.get(0);
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == '/') break;
            requestMethod += str.charAt(i);
        }

        requestMethod = requestMethod.replaceAll("\\s","");

        switch (requestMethod){
            case "GET":
                processGETRequest(request, clientWriteSource);
                break;
            case "POST":
                //processPOSTRequest(request);
                break;
            default:
                try {
                    clientWriteSource.writeUTF("Unsupported Request Method");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;
        }
    }

    public void processGETRequest(List<String> request, DataOutputStream clientWriteSource){
        String []imageExtensionArray = {"png", "jpg", "jpeg"};
        String []requestSplit = request.get(0).split(" ");
        String fileName = requestSplit[1];
        fileName = fileName.replaceAll("/", "");
        String fileExtension = fileName.split("[.]")[1].toLowerCase();
        if(Arrays.asList(imageExtensionArray).contains(fileExtension)) {
            sendImage(fileName, clientWriteSource);
        }else{
            sendFileName(fileName, clientWriteSource);
        }
    }

    public void sendFileName(String fileName, DataOutputStream clientWriteSource){
        try {
            int bytes = 0;
            File file = new File("C:\\Users\\HP\\IdeaProjects\\ServerSide\\src\\com\\company\\localStorage\\" + fileName);
            if(file.exists() && !file.isDirectory()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[4 * 1024];
                while ((bytes = fileInputStream.read(buffer)) != -1) {
                    clientWriteSource.write(buffer, 0, bytes);
                    clientWriteSource.flush();
                }
                fileInputStream.close();
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
                BufferedImage image = ImageIO.read(file);
                ImageIO.write(image,extension,clientWriteSource);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

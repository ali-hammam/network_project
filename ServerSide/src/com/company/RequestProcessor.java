package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HP on 5/11/2022.
 */
public class RequestProcessor extends Thread {
    private Socket client;
    private DataInputStream clientReadSource;
    private  DataOutputStream clientWriteSource;
    private DataSender dataSender = new DataSender();

    public RequestProcessor(Socket client, DataInputStream clientReadSource, DataOutputStream clientWriteSource){
        this.client = client;
        this.clientReadSource = clientReadSource;
        this.clientWriteSource = clientWriteSource;
    }

    public void requestAcceptor(Socket client, DataInputStream clientReadSource, DataOutputStream clientWriteSource){
        try {
            String str = "";
            List<String> request = new ArrayList<>();
            while(!str.equals("TERMINATE")) {
                str = clientReadSource.readUTF();
                request.add(str);
            }
            System.out.println(request.toString());
            processRequestType(request, clientWriteSource);

            clientWriteSource.close();
            clientReadSource.close();
            System.out.println("Client " + client + " closed connection...");
            client.close();
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
                try {
                    if(dataSender.isGETError()){
                        clientWriteSource.writeUTF("HTTP/1.0 404 " + getFileName(request, clientWriteSource)
                                + " Not Found\r\n");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
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
            dataSender.sendImage(fileName, clientWriteSource);
        }else{
            dataSender.sendFileName(fileName, clientWriteSource);
        }
    }

    public String getFileName(List<String> request, DataOutputStream clientWriteSource){
        String []requestSplit = request.get(0).split(" ");
        String fileName = requestSplit[1];
        return fileName.replaceAll("/", "");
    }
    public void run(){
        this.requestAcceptor(this.client, this.clientReadSource, this.clientWriteSource);
    }
}

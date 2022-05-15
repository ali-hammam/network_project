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
    private boolean isThreadAlive = false;

    public RequestProcessor(Socket client, DataInputStream clientReadSource, DataOutputStream clientWriteSource){
        this.client = client;
        this.clientReadSource = clientReadSource;
        this.clientWriteSource = clientWriteSource;
    }

    public void setClientReadSource(DataInputStream clientReadSource) {
        this.clientReadSource = clientReadSource;
    }

    public void setClientWriteSource(DataOutputStream clientWriteSource) {
        this.clientWriteSource = clientWriteSource;
    }

    public void requestAcceptor(){
        try {
            String str = "";
            List<String> request = new ArrayList<>();
            while(!str.equals("TERMINATE")) {
                System.out.println(str);
                str = this.clientReadSource.readUTF();
                String setting = str.split(":")[0];
                if(setting.equals("Connection")){
                    this.isThreadAlive = true;
                    Server.clientIpThread.put(Server.getClientRemoteIp(client), this);
                    //Server.clientIpThread.get(Server.getClientRemoteIp(client)).start();
                }
                request.add(str);
            }
            System.out.println(request.toString());
            processRequestType(request, this.clientWriteSource, this.clientReadSource);

            /*clientWriteSource.close();
            clientReadSource.close();
            System.out.println("Client " + client + " closed connection...");
            client.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequestType(List<String> request, DataOutputStream clientWriteSource, DataInputStream clientReadSource){
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
                                + " Not Found\\r\\n");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case "POST":
                processPOSTRequest(request, clientReadSource);
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

    public boolean isThreadAlive() {
        return isThreadAlive;
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

    public void processPOSTRequest(List<String> request, DataInputStream payload){
        try {
            String []imageExtensionArray = {"png", "jpg", "jpeg"};
            String []requestSplit = request.get(0).split(" ");
            String fileName = requestSplit[1];
            fileName = fileName.replaceAll("/", "");
            String fileExtension = fileName.split("[.]")[1].toLowerCase();
            if(Arrays.asList(imageExtensionArray).contains(fileExtension)) {
                DataReceiver.receiveImage(fileName, payload);
            }else {
                DataReceiver.receiveFile(fileName, payload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileName(List<String> request, DataOutputStream clientWriteSource){
        String []requestSplit = request.get(0).split(" ");
        String fileName = requestSplit[1];
        return fileName.replaceAll("/", "");
    }

    @Override
    public String toString() {
        return client.toString();
    }

    public void setThreadAlive(boolean val){
        this.isThreadAlive = val;
    }

    public void run(){
        try {
            this.requestAcceptor();
            if(isThreadAlive()) {
                Thread.sleep(15000);
                Server.clientIpThread.remove(client.getRemoteSocketAddress().toString().split(":")[0]);
            }
            this.clientWriteSource.close();
            this.clientReadSource.close();
            System.out.println("Client " + client + " closed connection...");
            this.client.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

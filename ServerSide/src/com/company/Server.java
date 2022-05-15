package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by HP on 5/8/2022.
 */
public class Server {
    private ServerSocket server = null;
    private RequestProcessor request = null;
    public static Map<String, RequestProcessor> clientIpThread;
    public Server(){
        clientIpThread = new HashMap<>();
        try {
            this.server = new ServerSocket(8000);
            while(true){
                Socket client = server.accept();

                 //it will read the data as string or int or any type as you want
                DataOutputStream clientWriteSource = new DataOutputStream(client.getOutputStream());
                DataInputStream clientReadSource = new DataInputStream(client.getInputStream());

                if (clientIpThread.containsKey(getClientRemoteIp(client))) {
                    System.out.println("Thread already assigned");
                    clientIpThread.get(getClientRemoteIp(client)).setClientReadSource(clientReadSource);
                    clientIpThread.get(getClientRemoteIp(client)).setClientWriteSource(clientWriteSource);
                    clientIpThread.get(getClientRemoteIp(client)).requestAcceptor();
                } else {
                    System.out.println("Thread assigned");
                    RequestProcessor thread = new RequestProcessor(client, clientReadSource, clientWriteSource);
                    if(thread.isThreadAlive()) {
                        clientIpThread.put(getClientRemoteIp(client), thread);
                        clientIpThread.get(getClientRemoteIp(client)).start();
                    }else{
                        thread.start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {

            this.server = new ServerSocket(8000);
            while(true){
                Socket client = server.accept();

                DataInputStream clientReadSource = new DataInputStream(client.getInputStream()); //it will read the data as string or int or any type as you want
                DataOutputStream clientWriteSource = new DataOutputStream(client.getOutputStream());

                System.out.println("Thread assigned");
                Thread thread = new RequestProcessor(client, clientReadSource, clientWriteSource);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void printHashMap(Map<String, RequestProcessor> map){
        for (String name: map.keySet()) {
            String value = map.get(name).toString();
            System.out.println(value + " " + value);
        }
    }

    public static String getClientRemoteIp(Socket client){
        return client.getRemoteSocketAddress().toString().split(":")[0];
    }

    public static boolean checkIfConnectionPersistent(Socket client, DataInputStream clientReadSource){
        try {
            String str = "";
            while (!str.equals("TERMINATE")) {
                str = clientReadSource.readUTF();
                String setting = str.split(":")[0];
                if (setting.equals("Connection")) {
                    return true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}

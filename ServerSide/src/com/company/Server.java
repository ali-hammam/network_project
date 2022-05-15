package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    }

    public static String getClientRemoteIp(Socket client){
        return client.getRemoteSocketAddress().toString().split(":")[0];
    }
}

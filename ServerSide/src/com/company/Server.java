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
    private ServerSocket server = null;
    private RequestProcessor request = null;
    public Server(){
        try {

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
        }
    }
}

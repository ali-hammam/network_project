package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
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

            String str = otherReadSource.readUTF();
            System.out.println(str);

            switch (requestMethod){
                case "get":{
                    String fileExtension = fileName.split("[.]")[1].toLowerCase();
                    makeGetRequest(otherWriteSource, fileName, ip.toString());
                    if(Arrays.asList(imageExtensionArray).contains(fileExtension)) {
                        receiveImage(fileName, otherReadSource);
                    }else{
                        receiveFile(fileName, otherReadSource);
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

    public static void receiveFile(String fileName, DataInputStream dataInputStream) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[4*1024];
        while ((bytes = dataInputStream.read(buffer, 0, 4)) != -1) {
            fileOutputStream.write(buffer,0,bytes);
        }
        fileOutputStream.close();
    }

    public void receiveImage(String fileName, DataInputStream dataInputStream) throws Exception {
        BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(dataInputStream));
        File outputfile = new File(fileName);
        ImageIO.write(image, "PNG", outputfile);
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }
}

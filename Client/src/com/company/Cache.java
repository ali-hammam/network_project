package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 5/12/2022.
 */
public class Cache {
    public static void receiveFileFromCache(String fileName) throws Exception{
        try {
            int i;
            File myObj = new File(fileName);
            BufferedReader myReader = new BufferedReader(new FileReader(myObj));
            while ((i = myReader.read()) != -1) {
                System.out.print((char)i);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void receiveImageFromCache(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
            ImageIcon icon=new ImageIcon(image);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(1080,720);
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCache(int time){
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        List<String> cacheFiles = listCacheFile();
                        for(String fileName : cacheFiles){
                            File file = new File(fileName);
                            file.delete();
                        }
                    }
                },
                time
        );
    }

    public static List<String> listCacheFile(){
        File folder = new File("D:\\network_project\\Client");
        File[] listOfFiles = folder.listFiles();
        List<String> cacheFiles = new ArrayList<>();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile() && !isIml(listOfFile.getName())) {
                cacheFiles.add(listOfFile.getName());
            }
        }

        return cacheFiles;
    }

    private static boolean isIml(String fileName){
        String fileExtension = fileName.split("[.]")[1].toLowerCase();
        return fileExtension.equals("iml");
    }
}

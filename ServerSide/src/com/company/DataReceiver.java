package com.company;

import com.sun.deploy.util.ArrayUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by HP on 5/11/2022.
 */
public class DataReceiver {
    public static void receiveFile(String fileName, DataInputStream dataInputStream) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\network_project\\ServerSide\\src\\com\\company\\localStorage\\" + fileName);
        byte[] buffer = new byte[4*1024];
        while ((bytes = dataInputStream.read(buffer, 0, 4)) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            System.out.println((char)bytes);
        }

        fileOutputStream.close();
    }

    public static void receiveImage(String fileName, DataInputStream dataInputStream) throws Exception {
        BufferedImage image = ImageIO.read(ImageIO.createImageInputStream(dataInputStream));
        File outputfile = new File("D:\\network_project\\ServerSide\\src\\com\\company\\localStorage\\" + fileName);
        ImageIO.write(image, "PNG", outputfile);
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }
}

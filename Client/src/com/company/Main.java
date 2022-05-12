package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*GET Requests*/
        Cache.clearCache(1);
        boolean flag = true;
        while (flag){
            ClientRequest request = null;
            System.out.println("WELCOME");
            System.out.println("1. Make GET Request");
            System.out.println("2. Make POST Request");
            System.out.println("3. Exit\n");
            Scanner input = new Scanner(System.in);
            String x = input.nextLine();
            switch (x){
                case "1":{
                    System.out.print("Enter The file You want to GET/ ");
                    String fileName = input.next();
                    request = new ClientRequest("get", fileName);
                    request.start();
                    break;
                }

                case "2":{
                    System.out.print("Enter The file You want to POST/ ");
                    String fileName = input.next();
                    request = new ClientRequest("post", fileName);
                    request.start();
                    break;
                }

                case "3":{
                    flag = false;
                    break;
                }

                default:{
                    System.out.println("Enter A Correct Input");
                    break;
                }
            }
            Cache.clearCache(12000);
        }
    }

}

package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*GET Requests*/
        Cache.clearCache(1);
        boolean flag = true;
        while (flag){
            ClientRequest request = null;
            System.out.println("1. Make GET Request");
            System.out.println("2. Make POST Request");
            System.out.println("3. Make Persistent GET Request");
            System.out.println("4. Exit\n");
            Scanner input = new Scanner(System.in);
            String x = input.nextLine();
            switch (x){
                case "1":{
                    System.out.print("Enter The file You want to GET/ ");
                    String fileName = input.next();
                    request = new ClientRequest("get", fileName, false);
                    request.start();
                    break;
                }

                case "2":{
                    System.out.print("Enter The file You want to POST/ ");
                    String fileName = input.next();
                    request = new ClientRequest("post", fileName,false);
                    request.start();
                    break;
                }
                case "3":{
                    long endTime = System.currentTimeMillis() + 15000;
                    while (System.currentTimeMillis() < endTime) {
                        System.out.println("1. choose GET request");
                        System.out.println("2. choose POST request");
                        String GETORPOST = input.next();
                        switch (GETORPOST){
                            case "1" : {
                                System.out.print("GET/ ");
                                String fileName = input.next();
                                request = new ClientRequest("get", fileName, true);
                                request.start();
                                break;
                            }

                            case "2": {
                                System.out.print("POST/ ");
                                String fileName = input.next();
                                request = new ClientRequest("post", fileName, true);
                                request.start();
                                break;
                            }

                            default: {
                                System.out.println("Enter a valid number before TIMEOUT");
                                break;
                            }
                        }
                    }
                    System.out.println("Connection TIMEOUT");
                    break;
                }

                case "4":{
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

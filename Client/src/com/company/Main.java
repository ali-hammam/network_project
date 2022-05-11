package com.company;

public class Main {

    public static void main(String[] args) {
	    ClientRequest request1 = new ClientRequest("get", "fig.PNG");
        ClientRequest request2 = new ClientRequest("get", "ali.txt");
        ClientRequest request3 = new ClientRequest("get", "hh.txt");
        ClientRequest request4 = new ClientRequest("get", "hffh.txt");
        request1.start();
        request2.start();
        request3.start();
        request4.start();

    }

    public static void executeThread(){
        for(int i = 0; i < 3; i++){

        }
    }
}

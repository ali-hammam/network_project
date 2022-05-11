package com.company;

public class Main {

    public static void main(String[] args) {
        /*GET Requests*/
	    /*ClientRequest request1 = new ClientRequest("get", "fig.PNG");
        ClientRequest request2 = new ClientRequest("get", "ali.txt");
        ClientRequest request3 = new ClientRequest("get", "hh.txt");
        ClientRequest request4 = new ClientRequest("get", "hffh.txt");
        request1.start();
        request2.start();
        request3.start();
        request4.start();*/

	    /*POST Requests*/
        ClientRequest request4 = new ClientRequest("post", "D:\\test.PNG");
        request4.start();
    }
}

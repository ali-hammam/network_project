package com.company;

/**
 * Created by HP on 5/11/2022.
 */
public class ClientRequest extends Thread{
    private String methodType;
    private String file;

    public ClientRequest(String methodType, String file){
        this.methodType = methodType;
        this.file = file;
    }

    public void run(){
        new Client(this.methodType, this.file);
    }
}

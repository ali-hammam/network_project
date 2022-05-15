package com.company;

/**
 * Created by HP on 5/11/2022.
 */
public class ClientRequest extends Thread{
    private String methodType;
    private String file;
    private boolean isPersistent;

    public ClientRequest(String methodType, String file, boolean isPersistent){
        this.methodType = methodType;
        this.file = file;
        this.isPersistent = isPersistent;
    }

    public void run(){
           new Client(this.methodType, this.file, this.isPersistent);
    }
}

package com.bitshammer.infra.restclient;

import lombok.Data;

@Data
public class Response {
    int statusCode;
    String data;

    public Response(int statusCode, String data){
        this.statusCode = statusCode;
        this.data = data;
    }



    public void throwIfError(String message){
        if(statusCode != 200) {
            throw new RuntimeException(String.format("Code:[%d] Data:[%s] Message:[%s]", statusCode, data, message));
        }
    }
}

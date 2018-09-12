package com.bitshammer.reports;

import spark.Request;
import spark.Response;

import javax.inject.Singleton;


@Singleton
public class ReportsController {

    public Object juridico(Request request, Response response){
        return "report generated!";
    }
}

package com.bitshammer.reports;

import spark.Request;
import spark.Response;

public class ReportsController {

    public Object juridico(Request request, Response response){
        return "report generated!";
    }
}

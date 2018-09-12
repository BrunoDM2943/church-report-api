package com.bitshammer.infra;

import com.bitshammer.reports.ReportsController;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
public class AppRoutes {

    @Inject
    private ReportsController reportsController;

    public void setUpRoutes() {
        get("/reports/juridico", reportsController::juridico);
    }
}

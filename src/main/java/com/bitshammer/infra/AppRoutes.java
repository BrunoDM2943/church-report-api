package com.bitshammer.infra;

import com.bitshammer.reports.ReportsController;
import com.bitshammer.security.JWTFilter;
import spark.Filter;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.before;
import static spark.Spark.get;

@Singleton
public class AppRoutes {

    @Inject
    private ReportsController reportsController;

    @Inject
    private JWTFilter jwtFilter;

    public void setUpRoutes() {
        before(jwtFilter::validateToken);
        get("/reports/juridico", reportsController::juridico);
    }
}

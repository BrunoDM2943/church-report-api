package  com.bitshammer;

import com.bitshammer.reports.ReportsController;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkMain {

    public static void main(String[] args) {
        int port = 8080;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null)
            port = Integer.parseInt(processBuilder.environment().get("PORT"));

        ReportsController reportsController = new ReportsController();
        port(port);
        get("/ping", (req, res) -> "pong");
        get("/reports/juridico", reportsController::juridico);

    }
}

package  com.bitshammer;

import com.bitshammer.infra.AppRoutes;
import com.bitshammer.infra.WeldConfiguration;

import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkMain {

    public static void main(String[] args) {
        int port = 8080;
        Logger.getGlobal().info("Init!");
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null)
            port = Integer.parseInt(processBuilder.environment().get("PORT"));
        WeldConfiguration.init();
        AppRoutes appRoutes = WeldConfiguration.getBean(AppRoutes.class);
        port(port);
        get("/ping", (req, res) -> "pong");
        appRoutes.setUpRoutes();
        Logger.getGlobal().info("Routes Open!");

    }
}

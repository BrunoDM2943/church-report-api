package  com.bitshammer;

import com.bitshammer.infra.AppRoutes;
import com.bitshammer.reports.ReportsController;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkMain {

    public static void main(String[] args) {
        int port = 8080;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null)
            port = Integer.parseInt(processBuilder.environment().get("PORT"));
        Weld weld = new Weld()
                .disableDiscovery()
                .addPackages(true, SparkMain.class.getPackage())
                .property("org.jboss.weld.construction.relaxed", true);
        AppRoutes appRoutes;
        try (WeldContainer container = weld.initialize()) {
            appRoutes = container.select(AppRoutes.class).get();
        }



        port(port);
        get("/ping", (req, res) -> "pong");
        appRoutes.setUpRoutes();

    }
}

package  com.bitshammer;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkMain {

    public static void main(String[] args) {
        int port = 8080;
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null)
            port = Integer.parseInt(processBuilder.environment().get("PORT"));

        port(port);
        get("/ping", (req, res) -> "pong");
    }
}

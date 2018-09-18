package com.bitshammer.reports;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import spark.Request;
import spark.Response;


@Singleton
public class ReportsController {

    public HttpServletResponse juridico(Request req, Response res) throws Exception {
        // Compile jrxml file.
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("reports/juridica.jrxml");
        JasperReport jasperReport = JasperCompileManager
                .compileReport(inputStream);
        // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();
        URL url = new URL("https://disciples-api.herokuapp.com/api/disciples/report/membro");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        StringBuilder builder = new StringBuilder();
        String output = null;
        while ((output = br.readLine()) != null) {
            builder.append(output);
        }

        conn.disconnect();
        List<MembroReportDTO> membros = new Gson().fromJson(builder.toString(), new TypeToken<ArrayList<MembroReportDTO>>(){}.getType());
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(membros);

        // DataSource
        // This is simple example, no database.
        // then using empty datasource.
        JRDataSource dataSource = itemsJRBean;

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Make sure the output directory exists.

        // Export to PDF.
        ServletOutputStream outputStream = res.raw().getOutputStream();
        res.raw().setContentType("application/octet-stream");
        res.raw().setHeader("Content-Disposition","attachment; filename=teste.pdf");
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        outputStream.flush();
        outputStream.close();
        System.out.println("Done!");
        return res.raw();
    }

}

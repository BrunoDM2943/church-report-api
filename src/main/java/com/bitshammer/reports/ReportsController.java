package com.bitshammer.reports;

import com.bitshammer.model.MembroReportDTO;
import com.bitshammer.resports.jasper.JasperService;
import com.bitshammer.rest.ChurchMembersAPI;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;


@Singleton
public class ReportsController {

    @Inject
    private ChurchMembersAPI churchMembersAPI;

    @Inject
    private JasperService jasperService;

    @Inject
    private ReportsService reportsService;

    public HttpServletResponse juridico(Request req, Response res) throws Exception {
        List<MembroReportDTO> membros = churchMembersAPI.getMembers();
        membros.forEach(m -> {
            int idade = LocalDate.now().getYear() - m.getDtNascimento().getYear();
            if (idade < 15) {
                m.setClassificacao("Criança");
            } else if (idade < 18) {
                m.setClassificacao("Adolescente");
            } else if (idade < 30 && m.getDtCasamento() == null) {
                m.setClassificacao("Jovem");
            } else {
                m.setClassificacao("Adulto");
            }
        });
        byte[] report = jasperService.generateReport("reports/juridica.jrxml", membros);
        ServletOutputStream outputStream = res.raw().getOutputStream();
        res.raw().setContentType("application/octet-stream");
        res.raw().setHeader("Content-Disposition","attachment; filename=relatorioMembros.pdf");
        res.raw().getOutputStream().write(report);
        outputStream.flush();
        outputStream.close();
        return res.raw();
    }

    public HttpServletResponse nascimento(Request req, Response res) throws Exception {
        List<MembroReportDTO> membros = churchMembersAPI.getMembers();
        byte[] report = reportsService.generateListaAniversariantes(membros);
        ServletOutputStream outputStream = res.raw().getOutputStream();
        res.raw().setContentType("application/octet-stream");
        res.raw().setHeader("Content-Disposition","attachment; filename=aniversariantes.xls");
        res.raw().getOutputStream().write(report);
        outputStream.flush();
        outputStream.close();
        return res.raw();
    }

    public HttpServletResponse casamento(Request req, Response res) throws Exception {
        List<MembroReportDTO> membros = churchMembersAPI.getMembers();
        byte[] report = reportsService.generateListaAniversariantesCasamento(membros);
        ServletOutputStream outputStream = res.raw().getOutputStream();
        res.raw().setContentType("application/octet-stream");
        res.raw().setHeader("Content-Disposition","attachment; filename=aniversariantesCasamento.xls");
        res.raw().getOutputStream().write(report);
        outputStream.flush();
        outputStream.close();
        return res.raw();
    }


}

package com.bitshammer.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReportsController {

    @Autowired
    ReportsService reportsService;

    @RequestMapping("/reports/members")
    public ResponseEntity<Resource> juridico() throws Exception {
        byte[] report = reportsService.generateMembersReport();
        return ResponseEntity.ok()
                .header("Content-Disposition","attachment")
                .header("filename", "relatorioMembros.pdf")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(report));
    }

    @RequestMapping("/reports/aniversariantes/nascimento")
    public ResponseEntity<Resource> nascimento() throws Exception {
        byte[] report = reportsService.generateBirthdayList();
        return ResponseEntity.ok()
                .header("Content-Disposition","attachment")
                .header("filename", "aniversariantes.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(report));
    }

    @RequestMapping("/reports/aniversariantes/casamento")
    public ResponseEntity<Resource> casamento() throws Exception {
        byte[] report = reportsService.generateMerrydayList();
        return ResponseEntity.ok()
                .header("Content-Disposition","attachment")
                .header("filename", "aniversariantesCasamento.xls")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(report));
    }


}

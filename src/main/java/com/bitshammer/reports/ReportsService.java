package com.bitshammer.reports;

import com.bitshammer.client.churchMembers.ChurchMembersAPI;
import com.bitshammer.client.churchMembers.MembersSearchResponse;
import com.bitshammer.model.MembroReportDTO;
import com.bitshammer.reports.jasper.JasperService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    @Autowired
    private ChurchMembersAPI churchMembersAPI;

    @Autowired
    private JasperService jasperService;

    byte[] generateBirthdayList() throws IOException {
        List<MembersSearchResponse> members = churchMembersAPI.getMembers();
        Workbook wb = new HSSFWorkbook();
        Sheet sheetDefault = wb.createSheet();
        Map<Month, List<MembersSearchResponse>> mapMembros = generateMapMember(members, (member) -> member.getPessoa().getDtNascimento());
        int rowNum = 0;
        for (Month month : mapMembros.keySet()) {
            sheetDefault.createRow(rowNum++).createCell(0).setCellValue(month.getDisplayName(TextStyle.FULL, new Locale("pt", "Br")));
            for (MembersSearchResponse dto : mapMembros.get(month)) {
                Row row = sheetDefault.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getPessoa().getNome());
                row.createCell(1).setCellValue(dto.getPessoa().getDtNascimento().format(DateTimeFormatter.ofPattern("dd/MM")));
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    byte[] generateMembersReport() throws Exception {
        List<MembersSearchResponse> members = churchMembersAPI.getMembers();
        members.sort((o1, o2) -> o1.getPessoa().getNome().compareToIgnoreCase(o2.getPessoa().getNome()));
        return jasperService.generateReport("reports/juridica.jrxml", members.stream().map(
                MembroReportDTO::fromAPI
        ).collect(Collectors.toList()));

    }

    byte[] generateMerrydayList() throws IOException {
        List<MembersSearchResponse> members = churchMembersAPI.getMembers();
        Workbook wb = new HSSFWorkbook();
        Sheet sheetDefault = wb.createSheet();
        members = members.stream().filter(m -> m.getPessoa().getDtCasamento() != null).collect(Collectors.toList());
        Map<Month, List<MembersSearchResponse>> mapMembros = generateMapMember(members, (member) -> member.getPessoa().getDtCasamento());
        int rowNum = 0;
        for (Month month : mapMembros.keySet()) {
            sheetDefault.createRow(rowNum++).createCell(0).setCellValue(month.getDisplayName(TextStyle.FULL, new Locale("pt", "Br")));
            for (MembersSearchResponse dto : mapMembros.get(month)) {
                Row row = sheetDefault.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getPessoa().getNome() + "&" + dto.getPessoa().getConjuge());
                row.createCell(1).setCellValue(dto.getPessoa().getDtCasamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private Map<Month, List<MembersSearchResponse>> generateMapMember(Collection<MembersSearchResponse> membros, Function<MembersSearchResponse, LocalDate> function) {
        Comparator<LocalDate> localDateComparator = Comparator.comparing(LocalDate::getMonth).thenComparing(LocalDate::getDayOfMonth);
        return membros.stream()
                .sorted((m1, m2) -> localDateComparator.compare(function.apply(m1), function.apply(m2)))
                .collect(Collectors.groupingBy(
                        (MembersSearchResponse key) -> function.apply(key).getMonth(),
                        LinkedHashMap::new,
                        Collectors.mapping(Function.identity(), Collectors.toList())
                ));
    }

}

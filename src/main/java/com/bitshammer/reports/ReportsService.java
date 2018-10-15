package com.bitshammer.reports;

import com.bitshammer.model.MembroReportDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class ReportsService {
    public byte[] generateListaAniversariantes(List<MembroReportDTO> membros) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheetDefault = wb.createSheet();
        Map<Month, List<MembroReportDTO>> mapMembros = generateMapMember(membros);
        int rowNum = 0;
        for (Month month : mapMembros.keySet()) {
            sheetDefault.createRow(rowNum++).createCell(0).setCellValue(month.getDisplayName(TextStyle.FULL, new Locale("pt", "Br")));
            for (MembroReportDTO dto : mapMembros.get(month)) {
                Row row = sheetDefault.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getNome());
                row.createCell(1).setCellValue(dto.getDtNascimento().format(DateTimeFormatter.ofPattern("dd/MM")));
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wb.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private Map<Month, List<MembroReportDTO>> generateMapMember(List<MembroReportDTO> membros) {
        Comparator<LocalDate> localDateComparator = Comparator.comparing(LocalDate::getMonth).thenComparing(LocalDate::getDayOfMonth);
        return membros.stream()
                .sorted((m1, m2) -> localDateComparator.compare(m1.getDtNascimento(), m2.getDtNascimento()))
                .collect(Collectors.groupingBy(
                        (MembroReportDTO key) -> key.getDtNascimento().getMonth(),
                        LinkedHashMap::new,
                        Collectors.mapping(Function.identity(), Collectors.toList())
                ));
    }

}

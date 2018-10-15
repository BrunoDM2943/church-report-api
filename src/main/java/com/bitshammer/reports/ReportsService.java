package com.bitshammer.reports;

import com.bitshammer.model.MembroReportDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class ReportsService {
    public byte[] generateListaAniversariantes(List<MembroReportDTO> membros) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheetDefault = wb.createSheet();
        Map<Month, List<MembroReportDTO>> mapMembros = generateMapMember(membros);
        StringBuilder builder = new StringBuilder();
        builder.append("NOME;DATA\n");
        for (Month month : mapMembros.keySet()) {
            builder.append(month.toString() + "\n");
            for (MembroReportDTO dto : mapMembros.get(month)) {
                builder.append(String.format("%s;%s\n", dto.getNome(), dto.getDtNascimento().format(DateTimeFormatter.ofPattern("dd/MM"))));
            }
        }

        return builder.toString().getBytes();
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

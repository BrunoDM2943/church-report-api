package com.bitshammer.reports;

import com.bitshammer.model.MembroReportDTO;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Singleton
public class ReportsService {
    public byte[] generateListaAniversariantes(List<MembroReportDTO> membros) {

        StringBuilder builder = new StringBuilder();
        builder.append("NOME;DATA\n");
        membros.stream().sorted((m1, m2) -> {

            LocalDate d1 = LocalDate.parse(m1.getDtNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate d2 = LocalDate.parse(m2.getDtNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return Comparator.comparing(LocalDate::getMonth).thenComparing(LocalDate::getDayOfMonth).compare(d1, d2);
        }).forEachOrdered((m) -> builder.append(String.format("%s;%s\n", m.getNome(), m.getDtNascimento())));
        return builder.toString().getBytes();
    }

}

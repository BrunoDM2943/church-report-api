package com.bitshammer.reports;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

    @Builder
    @Data
    @Getter
    public class MembroReportDTO {
        private String nome;
        private String endereco;
        private String dtNascimento;
        private String dtCasamento;
        private String telefone;
        private String celular;
        private String email;
        private String classificacao;

        public static String obterClassificao(LocalDate dtNascimento){
            long idade = dtNascimento.until(LocalDate.now(), ChronoUnit.YEARS);
            if(idade < 15){
                return "Criança";
            } else if(idade < 30){
                return "Jovem";
            } else {
                return "Adulto";
            }
        }



}

package com.bitshammer.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
    @Data
    @Getter
    public class MembroReportDTO {
        private String nome;
        private String endereco;
        private LocalDate dtNascimento;
        private LocalDate dtCasamento;
        private String telefone;
        private String celular;
        private String email;
        private String classificacao;
        private String conjuge;
        private String cep;

        public String getDtNascimentoFmt() {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dtNascimento);
        }

        public String getDtCasamentoFmt() {
            if(dtCasamento == null) {
                return "";
            }
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dtCasamento);
        }
}

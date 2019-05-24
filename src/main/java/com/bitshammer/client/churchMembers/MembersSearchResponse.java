package com.bitshammer.client.churchMembers;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class MembersSearchResponse {

    private String classificacao;
    private Pessoa pessoa;

    @Data
    private static class Pessoa {
        private String nome;
        private String conjuge;
        private LocalDate dtNascimento;
        private LocalDate dtCasamento;
        private Endereco endereco;
        private Contato contato;

        @Data
        private class Contato{
            private String telefone;
            private String celular;
            private String email;
        }

        @Data
        private static class Endereco {
            private String endereco;
        }

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
}

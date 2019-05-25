package com.bitshammer.client.churchMembers;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class MembersSearchResponse {

    private String classificacao;
    private Pessoa pessoa;

    @Data
    public static class Pessoa {
        private String nome;
        private String conjuge;
        private LocalDate dtNascimento;
        private LocalDate dtCasamento;
        private Endereco endereco;
        private Contato contato;

        @Data
        public class Contato{
            private String telefone;
            private String celular;
            private String email;
        }

        @Data
        public static class Endereco {
            private String endereco;
        }
    }
}

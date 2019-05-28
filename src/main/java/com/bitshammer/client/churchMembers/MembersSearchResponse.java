package com.bitshammer.client.churchMembers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembersSearchResponse {

    private String classificacao;
    private Pessoa pessoa;

    @Data
    @Builder
    public static class Pessoa {
        private String nome;
        private String conjuge;
        private LocalDate dtNascimento;
        private LocalDate dtCasamento;
        private Endereco endereco;
        private Contato contato;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Contato{
            private String telefone;
            private String celular;
            private String email;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Endereco {
            private String endereco;
        }
    }
}

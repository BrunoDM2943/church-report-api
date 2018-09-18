package com.bitshammer.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
}

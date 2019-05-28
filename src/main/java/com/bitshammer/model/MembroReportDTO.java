package com.bitshammer.model;

import com.bitshammer.client.churchMembers.MembersSearchResponse;
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

    public static MembroReportDTO fromAPI(MembersSearchResponse member) {
        return MembroReportDTO.builder()
                .nome(member.getPessoa().getNome())
                .endereco(member.getPessoa().getEndereco().getEndereco())
                .dtNascimento(member.getPessoa().getDtNascimento())
                .dtCasamento(member.getPessoa().getDtCasamento())
                .telefone(member.getPessoa().getContato().getTelefone())
                .celular(member.getPessoa().getContato().getCelular())
                .email(member.getPessoa().getContato().getEmail())
                .classificacao(member.getClassificacao())
                .conjuge(member.getPessoa().getConjuge())
                .build();

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

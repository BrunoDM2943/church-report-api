package com.bitshammer.rest;

import com.bitshammer.infra.RestCall;
import com.bitshammer.model.MembroReportDTO;
import com.google.gson.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChurchMembersAPI {

    @Inject
    private RestCall rest;

    public List<MembroReportDTO> getMembers(){
        List<MembroReportDTO> membrosDTO = new ArrayList<>();
        String url = "https://church-members-api.herokuapp.com/members";
        try {
            String response = rest.get(url);
            JsonParser parser = new JsonParser();
            JsonArray membersJson = parser.parse(response).getAsJsonArray();
            for(JsonElement it : membersJson) {
                JsonObject member = it.getAsJsonObject();
                JsonObject pessoa = member.getAsJsonObject("pessoa");
                JsonObject contato = pessoa.getAsJsonObject("contato");
                JsonObject endereco = pessoa.getAsJsonObject("endereco");
                membrosDTO.add(MembroReportDTO.builder()
                        .nome(getNome(pessoa))
                        .telefone(getTelefone(contato))
                        .celular(getCelular(contato))
                        .email(contato.get("email") != null ? contato.get("email").getAsString() : null)
                        .dtNascimento(getDate(pessoa.get("dtNascimento").getAsString()))
                        .dtCasamento(getDate(pessoa.get("dtCasamento").getAsString()))
                        .endereco(getEndereco(endereco))
                        .classificacao("")
                        .build());
            }
            membrosDTO.sort((c1,c2) -> c1.getNome().compareToIgnoreCase(c2.getNome()));
            return membrosDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTelefone(JsonObject contato) {
        if(contato.get("telefone") != null) {
            return String.format("(%d) %d", contato.get("dddTelefone").getAsInt(), contato.get("telefone").getAsInt());
        } else {
            return "";
        }

    }

    private String getCelular(JsonObject contato) {
        if(contato.get("celular") != null)
            return String.format("(%d) %d", contato.get("dddCelular").getAsInt(), contato.get("celular").getAsInt());
        return "";
    }

    private String getNome(JsonObject pessoa) {
        return String.format("%s %s", pessoa.get("nome").getAsString(), pessoa.get("sobrenome").getAsString());
    }

    private LocalDate getDate(String date){
        if("0001-01-01T00:00:00Z".equalsIgnoreCase(date)){
            return null;
        }
        return LocalDate.from(DateTimeFormatter.ISO_DATE_TIME.parse(date));
    }

    private String getEndereco(JsonObject endereco) {
        return String.format("%s, %d - %s", endereco.get("logradouro").getAsString(), endereco.get("numero").getAsInt(), endereco.get("bairro").getAsString());
    }
}

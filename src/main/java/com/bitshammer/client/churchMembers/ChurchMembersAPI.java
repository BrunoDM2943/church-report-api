package com.bitshammer.client.churchMembers;

import com.bitshammer.infra.rest.JsonFactory;
import com.bitshammer.infra.restclient.RESTClient;
import com.bitshammer.infra.restclient.Response;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChurchMembersAPI {

    public List<MembersSearchResponse> getMembers(){
        String url = "https://church-members-api.herokuapp.com/members/search";
        GraphQLWrapper<MembersSearchResponse> graphQLWrapper;
        Response response = RESTClient.post(url, "{\n" +
                "\tmember(active:true){\n" +
                "\t\tpessoa{\n" +
                "\t\t\tnome:fullName\n" +
                "\t\t\tconjuge: nomeConjuge\n" +
                "\t\t\tdtNascimento\n" +
                "\t\t\tdtCasamento\n" +
                "\t\t\tendereco{\n" +
                "\t\t\t\tendereco: full\n" +
                "\t\t\t}\n" +
                "\t\t\tcontato{\n" +
                "\t\t\t\ttelefone: phone\n" +
                "\t\t\t\tcelular: cellphone\n" +
                "\t\t\t\temail\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t}\n" +
                "\t\tclassificacao\n" +
                "\t}\n" +
                "}");
        response.throwIfError("Error calling members API");
        graphQLWrapper = JsonFactory.getGson().fromJson(response.getData(), new TypeToken<GraphQLWrapper<MembersSearchResponse>>(){}.getType());
        return graphQLWrapper.data.member;
    }

    private class GraphQLWrapper<T> {
        public Data<T> data;

        private class Data<T> {
            public List<T> member;
        }
    }
}

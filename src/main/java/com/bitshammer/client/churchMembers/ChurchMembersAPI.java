package com.bitshammer.client.churchMembers;

import com.bitshammer.infra.restclient.RestCall;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class ChurchMembersAPI {

    public List<MembersSearchResponse> getMembers(){
        String url = "https://church-members-api.herokuapp.com/members/search";
        RestCall rest = new RestCall();
        GraphQLWrapper<MembersSearchResponse> graphQLWrapper;
        try {
            String response = rest.post(url, "");
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                    ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDate()).create();
            graphQLWrapper = gson.fromJson(response, new TypeToken<GraphQLWrapper<MembersSearchResponse>>(){}.getType());
            //membrosDTO = new Gson().fromJson(response, new TypeToken<List<MembersSearchResponse>>(){}.getType());
            return graphQLWrapper.data.member;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GraphQLWrapper<T> {
        public Data<T> data;

        private class Data<T> {
            public List<T> member;
        }
    }
}

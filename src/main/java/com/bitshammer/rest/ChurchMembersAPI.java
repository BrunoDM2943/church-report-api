package com.bitshammer.rest;

import com.bitshammer.infra.RestCall;
import com.bitshammer.model.MembroReportDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChurchMembersAPI {

    @Inject
    private RestCall rest;

    public List<MembroReportDTO> getMembers(){
        String url = "https://disciples-api.herokuapp.com/api/disciples/report/membro";
        try {
            String response = rest.get(url);
            return new Gson().fromJson(response, new TypeToken<List<MembroReportDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

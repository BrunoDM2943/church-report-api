package com.bitshammer.client.churchMembers;

import com.bitshammer.infra.restclient.MockServer;
import com.bitshammer.infra.restclient.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestChurchMembersAPI {

    @Test
    public void TestPostSearchOK() throws IOException {
        MockServer.initMockServer();
        MockServer.onPost("https://church-members-api.herokuapp.com/members/search", new Response(200,String.join("", Files.readAllLines(Paths.get("src/test/resources/mock/search200.json")))));
        ChurchMembersAPI api = new ChurchMembersAPI();
        List<MembersSearchResponse> members = api.getMembers();
        Assertions.assertNotNull(members);
    }

    @Test
    public void TestPostSearchNOK() throws IOException {
        MockServer.initMockServer();
        MockServer.onPost("https://church-members-api.herokuapp.com/members/search", new Response(500,"Error"));
        ChurchMembersAPI api = new ChurchMembersAPI();
        Assertions.assertThrows(Exception.class, api::getMembers);
    }
}

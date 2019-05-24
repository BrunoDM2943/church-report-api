package com.bitshammer.client.churchMembers;

import com.bitshammer.infra.restclient.MockServer;
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

        MockServer.onPost("https://church-members-api.herokuapp.com/members/search", String.join("", Files.readAllLines(Paths.get("src/test/resources/mock/search200.json"))));
        ChurchMembersAPI api = new ChurchMembersAPI();
        List<MembersSearchResponse> members = api.getMembers();
        Assertions.assertNotNull(members);
    }
}

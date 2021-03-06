package com.bitshammer.infra.restclient;

import java.util.HashMap;
import java.util.Map;

public class MockServer {
    static final Map<String, Response> MOCK = new HashMap<>();

    static boolean MOCK_SERVER = false;

    public static void initMockServer() {
        MOCK_SERVER = true;
    }

    static boolean containsMock(String method, String call){
        return MOCK.containsKey(method + ":" + call);
    }

    static Response getMockedResponse(String method, String call) {
        return MOCK.get(method + ":" + call);
    }

    public static void onPost(String call, Response response) {
        MOCK.put("POST:" + call, response);
    }

}

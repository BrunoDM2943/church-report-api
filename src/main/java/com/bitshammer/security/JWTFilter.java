package com.bitshammer.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bitshammer.infra.restclient.RestCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import spark.Request;
import spark.Response;

import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static spark.Spark.halt;

@Singleton
public class JWTFilter {


    public void validateToken(Request request, Response response) throws Exception{
        String tokenStr = request.headers("Authorization");
        if(tokenStr == null) {
            halt(403, "Unauthorized");
        }
        try {
            tokenStr = tokenStr.split(" ")[1];
            DecodedJWT token = JWT.decode(tokenStr);
            RSAPublicKey publicKey = getPublicKey(token.getKeyId());
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey))
                    .withAudience("https://church-members-api")
                    .withIssuer("https://churchs.auth0.com/")
                    .build(); //Reusable verifier instance
            verifier.verify(tokenStr);

        } catch (Exception e){
            e.printStackTrace();
            halt(403, "Unauthorized");
        }
    }

    private RSAPublicKey getPublicKey(String keyId) throws Exception {
        RestCall rest = new RestCall();
        String response = null;
        try {
            response = rest.get("https://churchs.auth0.com/.well-known/jwks.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Jwks jwks = new Gson().fromJson(response, new TypeToken<Jwks>(){}.getType());
        for (JSONWebKeys key : jwks.keys) {
            if(key.kid.equalsIgnoreCase(keyId)){
                String cert = "-----BEGIN CERTIFICATE-----\n" + key.x5c[0] + "\n-----END CERTIFICATE-----";
                try {
                    Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(cert.getBytes()));
                    return (RSAPublicKey) certificate.getPublicKey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    @Data
    private static class Jwks{
         private List<JSONWebKeys> keys;
    }

    @Data
    private static class JSONWebKeys {
        String kty;
        String kid;
        String use;
        String n;
        String e;
        String[] x5c;
    }

}

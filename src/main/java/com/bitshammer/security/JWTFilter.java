package com.bitshammer.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bitshammer.infra.RestCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

public class JWTFilter {

    public static void main(String[] args) {
        JWTFilter filter = new JWTFilter();
        System.out.println(filter.validateToken());
    }

    private boolean validateToken(){
        String tokenStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik1qRkNORUZFUmtWQ1JVTkdNMFkzTlRBM09UVkdRMEpFUWtKQlFUWkdORFpDT0VKQ01FTkZNZyJ9.eyJpc3MiOiJodHRwczovL2NodXJjaHMuYXV0aDAuY29tLyIsInN1YiI6IjYyQTdKd0lTSG92NGxUMEZIclZyV1RGbm54eHVmWEJKQGNsaWVudHMiLCJhdWQiOiJodHRwczovL2NodXJjaC1tZW1iZXJzLWFwaSIsImlhdCI6MTUzNzEzNTc0NiwiZXhwIjoxNTM3MjIyMTQ2LCJhenAiOiI2MkE3SndJU0hvdjRsVDBGSHJWcldURm5ueHh1ZlhCSiIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyJ9.evtcGS27dZdM3tJWBBOaMrUQopzLcFsQnUsrav6Odi1nahEMbXrorWFzV2X-mg-SKU5_NvPqbB7xz1QznG1AEa1KNNxOeFpNrSHzjEULnqQyFFG-qTnUbbNL--_qRtKVcmihPTApp0pacnwl8L2Gd3FkacbeA4pDvfD6EMXlYJCzd6kIyAvTuz-s8cqfQM_ErhaDrqLNsKrOdDBRvKuLMhmdMAPDJlR3b5yAW9JJmHyYUgJKdgYkao415pvu4MrkoLKrsbM5tbIzyMnV8ox9m83X7k5YwHNdJdgGdxisFieXhHoV6oQBtwNL3jUzFRPfmfx-mt1Cs5wsxXXBIwMH8Q";
        try {
            DecodedJWT token = JWT.decode(tokenStr);
            RSAPublicKey publicKey = getPublicKey(token.getKeyId());
            JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey))
                    .withAudience("https://church-members-api")
                    .withIssuer("https://churchs.auth0.com/")
                    .build(); //Reusable verifier instance
            verifier.verify(tokenStr);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
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

package liveproject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredAPI {

    String sshkey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC5OKEvxXEvwZK5RjDkGQqrIdPSSh/ic+VWuo5gC3hA77+Jpk8i4UYjowcH+3/93uYpkuJph8QJdUR2Mz2RWiO1JQEqozkhgV8iFXWIfx+vE9VUJqUeqIRydWa81DmIExl0qhf4v16SwHVGsUJOaWKD3k07ESynp7ppV4+MWr87KAGLHYMqe1qW788zPV3giq6jU3pSkCdB8Rip5vIvLB+KhG9YPjyTyTUgtSTOmpSS7NOqqPKV7MBA+G0G2XAucFV5js2LUwJrq7chBxqSAou4Hh3Nfm46RXAgu3aw8aVjkPeyVBzvhMztlTdxHd/fo7dBmnPVB8xUYkvkcCohOivV";
    int sshid;
    RequestSpecification rs;

    @BeforeClass
    public void setUp() {
        rs = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com").addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token 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")
                .build();
    }

    @Test(priority = 1)
    public void postTest() {
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("title", "testkey");
        reqBody.put("key", sshkey);


        Response resp = given().spec(rs).body(reqBody)
                .when().post("/user/keys");
        System.out.println(resp.getBody().asPrettyString());
        sshid = resp.then().extract().path("id");
        resp.then().statusCode(201).body("key", equalTo(sshkey));

    }

    @Test(priority = 2)
    public void getTest() {

        Response resp = given().spec(rs).pathParam("keyId", sshid)
                .when().get("/user/keys/{keyId}");
        System.out.println(resp.getBody().asPrettyString());
        resp.then().statusCode(200).body("key", equalTo(sshkey));

    }

    @Test(priority = 3)
    public void deleteTest() {

        Response resp = given().spec(rs).pathParam("keyId", sshid)
                .when().delete("/user/keys/{keyId}");
        System.out.println(resp.getBody().asPrettyString());
        resp.then().statusCode(204);

    }

}

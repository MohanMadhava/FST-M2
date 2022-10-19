package liveproject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers Object
    Map<String, String> headers = new HashMap<>();

    //Resource path
    String resourcePath = "/api/users";

    //Generate a contract
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        //Add the headers
        headers.put("Content-Type", "application/json");

        //Create the JSON body for request  and response
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",123)
                .stringType("firstName","Mohan")
                .stringType("lastName","Manjunath")
                .stringType("email","saahil@example.com");

        //Write the Fragment to pact
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .headers(headers)
                .path(resourcePath)
                .body(requestResponseBody)
                .willRespondWith().status(201)
                .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName="UserProvider",port="8282")
    public void consumerTest()
    {
        //Base URI
        String requestURI = "http://localhost:8282"+resourcePath;

        //Request body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id",123);
        reqBody.put("firstName","Mohan");
        reqBody.put("lastName","Manjunath");
        reqBody.put("email","saahil@example.com");

        //Given Response
        given().headers(headers).body(reqBody).log().all().
                when().post(requestURI).
                then().statusCode(201).log().all();


    }

}

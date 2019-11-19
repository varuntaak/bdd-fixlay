package bdd.subs;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by i316946 on 24/10/19.
 */
public class CancelSubscriptionSteps {

    private String id;
    Response response;

    @Given("the server is up")
    public void step1(){
        RestAssured.port = 9001;
        get("/").then().body(containsString("Server is up and running!"));
    }

    @Given("A subscription is created in the system with [mobile]")
    public void step2(@Named("mobile") String mobile){
        List<String> mobile1 = get("/subrequest?mobile=" + mobile).jsonPath().getList("mobile");
        if (mobile1.size() > 0)
            return;
        String payload = "{\n" +
                "\t\"first_name\":\"Varun\",\n" +
                "\t\"last_name\":\"Tak\",\n" +
                "\t\"mobile\":\"%s\",\n" +
                "\t\"email\":\"varuntaak@gmail.com\",\n" +
                "\t\"address_line1\":\"#04-14, 1 Bukit Batok St 25\",\n" +
                "\t\"address_line2\":\"Parkview Apartments\",\n" +
                "\t\"address_postal\":\"658882\",\n" +
                "\t\"address_country\":\"sf\", \n" +
                "\t\"service_electrician\" : false, \n" +
                "\t\"service_plumbing\" : false,\n" +
                "\t\"service_handyman\" : true\n" +
                "}";
        payload = String.format(payload, mobile);
        Response response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/subrequest");
        response.then().statusCode(200);
    }

    @Given("activate subscription is called with [mobile] and a subscription_id")
    public void step3(@Named("mobile") String mobile){
        String payload = "{\n" +
                "\t\"mobile\":\"%s\",\n" +
                "\t\"subscription_id\": \"HHHWERWER9873\"\n" +
                "}";
        payload = String.format(payload, mobile);
        given().contentType(ContentType.JSON)
                .body(payload)
                .post("/activate").then().statusCode(200);
    }

    @When("the api cancel subscription is called using [mobile] and [secret_key]")
    public void step6(@Named("mobile") String mobile, @Named("secret_key") String secret_key){
        System.out.println("secret - " + secret_key);
        String payload = "{\n" +
                "\t\"mobile\":\"%s\"\n" +
                "}";
        if (!secret_key.equals("null")){
            payload = "{\n" +
                    "\t\"mobile\":\"%s\",\n" +
                    "\t\"secret_key\":\"%s\"\n" +
                    "}";
            payload = String.format(payload, mobile, secret_key);
        } else {
            payload = String.format(payload, mobile);
        }
        response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/cancel_sub");
        response.then().statusCode(200);
        String s = response.getBody().asString();
    }


    @Then("the server reply with [status_code]")
    public void step4(@Named("status_code") int status_code){
        response.then().statusCode(status_code);
        String s = response.getBody().asString();
        System.out.println("body: " + s);
    }

    @Then("the response body must have [body_status] and [message]")
    public void step5(@Named("body_status") String status, @Named("message") String message){
        String actual_status = response.jsonPath().getString("status");
        assertThat(actual_status, equalTo(status));
        String actual_message = response.jsonPath().getString("message");
        assertThat(actual_message, equalTo(message));

    }



}

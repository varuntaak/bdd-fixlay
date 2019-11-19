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
public class ActivateSubscriptionSteps {

    private String id;
    Response response;

    @Given("the server is up")
    public void step1(){
        RestAssured.port = 9001;
        get("/").then().body(containsString("Server is up and running!"));
    }

    @Given("A subscription is created in the system with [mobile]")
    public void step2(@Named("mobile") String mobile){
        response = get("/subrequest?mobile=" + mobile);
        List<String> mobile1 = response.jsonPath().getList("mobile");
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

    @When("activate subscription is called with [mobile] and a subscription_id")
    public void step3(@Named("mobile") String mobile){
        String payload = "{\n" +
                "\t\"mobile\":\"%s\",\n" +
                "\t\"subscription_id\": \"HHHWERWER9873\"\n" +
                "}";
        payload = String.format(payload, mobile);
        response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/activate");
    }

    @Then("the server reply with [status_code]")
    public void step4(@Named("status_code") int status_code){
        response.then().statusCode(status_code);
    }

    @Then("get subscription by [mobile] has status as [status]")
    public void step5(@Named("mobile") String mobile, @Named("status") String status){
        response = get("/subrequest?mobile=" + mobile);
        String s = response.getBody().asString();
        System.out.println("body: " + s);
        List<String> status1 = response.jsonPath().getList("status");
        assertThat(status1.get(0), equalTo(status));

    }

    @When("cancel subscription is called using [mobile]")
    public void step6(@Named("mobile") String mobile){
        String payload = "{\n" +
                "\t\"mobile\":\"%s\"\n" +
                "}";
        payload = String.format(payload, mobile);
        response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/cancel_sub");
    }

}

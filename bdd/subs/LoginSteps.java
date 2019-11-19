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
public class LoginSteps {

    private String id;
    Response response;

    @Given("the server is up")
    public void step1(){
        RestAssured.port = 9001;
        get("/").then().body(containsString("Server is up and running!"));
    }

    @Given("A new subscription is created in the system with [mobile1] and [email1]")
    public void step2(@Named("mobile1") String mobile, @Named("email1") String email){
        List<String> mobile1 = get("/subrequest?mobile=" + mobile).jsonPath().getList("mobile");
        if (mobile1.size() > 0)
            return;
        String payload = "{\n" +
                "\t\"first_name\":\"Varun\",\n" +
                "\t\"last_name\":\"Tak\",\n" +
                "\t\"mobile\":\"%s\",\n" +
                "\t\"email\":\"%s\",\n" +
                "\t\"address_line1\":\"#04-14, 1 Bukit Batok St 25\",\n" +
                "\t\"address_line2\":\"Parkview Apartments\",\n" +
                "\t\"address_postal\":\"658882\",\n" +
                "\t\"address_country\":\"sf\", \n" +
                "\t\"service_electrician\" : false, \n" +
                "\t\"service_plumbing\" : false,\n" +
                "\t\"service_handyman\" : true\n" +
                "}";
        payload = String.format(payload, mobile, email);
        Response response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/subrequest");
        response.then().statusCode(200);
    }

    @When("call the login api with [mobile] and [email]")
    public void step6(@Named("mobile") String mobile, @Named("email") String email){
        String payload = "{\n" +
                "\t\"mobile\":\"%s\",\n" +
                "\t\"email\":\"%s\"\n" +
                "}";
        payload = String.format(payload, mobile, email);
        response = given().contentType(ContentType.JSON)
                .body(payload)
                .post("/login");
    }

    @Then("the server reply with [status_code] here for login")
    public void step4(@Named("status_code") int status_code){
//        String s = response.getBody().asString();
//        System.out.println("body: " + s);
        response.then().statusCode(status_code);
    }

    @Then("the server respond with [status] and [message]")
    public void step5(@Named("status") String status, @Named("message") String message){
        String actual_status = response.jsonPath().getString("status");
        assertThat(actual_status, equalTo(status));
        String actual_message = response.jsonPath().getString("message");
        assertThat(actual_message, equalTo(message));
    }



}

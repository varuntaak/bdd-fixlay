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
 * Created by i316946 on 3/10/19.
 */
public class CheckAllSubscriptionStep {

    String id = "";
    double mobile;
    Response response;

    @Given("the server is up")
    public void step1(){
        RestAssured.port = 9001;
        get("/").then().body(containsString("Server is up and running!"));
    }

    @Given("the setup DB is done at $set_up_path")
    public void step2(@Named("set_up_path") String path){
        get(path).then().statusCode(200);
    }

    @Given("A new subscription is created in the system")
    public void step3(){
        mobile = Math.random();
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
        String s = response.jsonPath().get("sub_id");
        System.out.println(s);
        id = s;
    }

    @When("call the get subscription with mobile number")
    public void step4(){
        response = get("/subrequest?mobile=" + mobile);
    }

    @Then("the server reply with status code 200")
    public void step5(){
        response.then().statusCode(200);
    }

    @Then("the server response body has the subscription entry which is created above")
    public void step6(){
        List<String> _mobile = response.jsonPath().getList("mobile");
        assertThat(String.valueOf(this.mobile), equalTo(_mobile.get(0)));
//        System.out.println(response.body().asString());
    }


    public static double getRandomIntegerBetweenRange(double min, double max){
        double x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }
}

package steps.EmployeeTest;

import com.example.springboot.soap.interfaces.GetEmployeeByIdRequest;
import com.example.springboot.soap.interfaces.ObjectFactory;
import data.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import util.Marshall;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GetEmployeeByIdSteps {
    Response response;

    @Step("Send SOAP request to get employee by ID")
    public Response getByID(String body) {
        response = given()
                //.filter(new AllureRestAssured())
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "interfaces.soap.springboot.example.com/exampleSoapHttp/getEmployeeByIdRequest")
                .body(body)
                .post(Constants.LOCALHOST_BASE_URI);

        System.out.println("Response:\n" + response.prettyPrint());
        return response;

    }

    @Step("Marshal the request body to get employee by ID: {0}")
    public String getMarshalledResponse(long id) {
        ObjectFactory factory = new ObjectFactory();
        GetEmployeeByIdRequest getEmployeeByIdRequest = factory.createGetEmployeeByIdRequest();
        getEmployeeByIdRequest.setEmployeeId(id);
        return Marshall.marshallSoapRequest(getEmployeeByIdRequest);
    }


    @Step("Validate employee ID in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponseID(long id) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='employeeId' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + id + "']"));
        return this;
    }

    @Step("Validate employee name in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponseName(String name) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='name' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + name + "']"));
        return this;
    }

    @Step("Validate employee department in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponseDepartment(String department) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='department' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + department + "']"));
        return this;
    }

    @Step("Validate employee phone in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponsePhone(String phone) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='phone' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + phone + "']"));
        return this;
    }

    @Step("Validate employee address in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponseAddress(String address) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='address' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + address + "']"));
        return this;
    }

    @Step("Validate employee email in the response is {0}")
    public GetEmployeeByIdSteps validateGetEmployeeByIdResponseEmail(String email) {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='email' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + email + "']"));
        return this;
    }


    //------------------------------------------------------------------------------------------------------------
    @Step("Validate the status code of the response is 500")
    public GetEmployeeByIdSteps validateStatus() {
        Assert.assertEquals(response.getStatusCode(), Constants.STATUS_CODE_500);
        return this;
    }

    @Step("Validate the error message contains 'Source must not be null'")
    public GetEmployeeByIdSteps validateErrorMessage() {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='faultstring'][contains(text(), 'Source must not be null')]"));
        return this;
    }


}

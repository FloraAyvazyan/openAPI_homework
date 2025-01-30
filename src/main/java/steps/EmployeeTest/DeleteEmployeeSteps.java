package steps.EmployeeTest;

import com.example.springboot.soap.interfaces.DeleteEmployeeRequest;
import com.example.springboot.soap.interfaces.ObjectFactory;
import data.Constants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import static io.restassured.config.XmlConfig.xmlConfig;
import static org.hamcrest.Matchers.hasXPath;
import static util.Marshall.marshallSoapRequest;

public class DeleteEmployeeSteps {
    String body;
    Response response;

    @Step("Create SOAP request body for deleting employee with ID: {0}")
    public DeleteEmployeeSteps createDeleteRequestBody(long id){
        ObjectFactory objectFactory = new ObjectFactory();
        DeleteEmployeeRequest deleteEmployeeRequest = objectFactory.createDeleteEmployeeRequest();
        deleteEmployeeRequest.setEmployeeId(id);
        body = marshallSoapRequest(deleteEmployeeRequest);
        return this;
    }

    @Step("Send SOAP request to delete employee")
    public DeleteEmployeeSteps sendRequest(){
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        //.filter(new AllureRestAssured())
                        .declareNamespace("ns2", Constants.NS2_NAME_SPACE)
                        .declareNamespace("SOAP-ENV",  "http://schemas.xmlsoap.org/soap/envelope/")))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "interfaces.soap.springboot.example.com/exampleSoapHttp/deleteEmployeeRequest")
                .body(body)
                .post(Constants.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate that the HTTP status code is 200")
    public DeleteEmployeeSteps validateStatus(){
        Assert.assertEquals(response.getStatusCode(), Constants.OK_STATUS_CODE);
        return this;
    }


    @Step("Validate the delete confirmation message in response")
    public DeleteEmployeeSteps validateDeleteMessage() {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and namespace-uri()='" + Constants.NS2_NAME_SPACE +"']" +
                        "[text()='" +  Constants.CONTENT_DELETED_SUCCESSFULLY + "']"));
        return this;
    }

}

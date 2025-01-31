package steps.EmployeeTest;

import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.ObjectFactory;
import com.example.springboot.soap.interfaces.UpdateEmployeeRequest;
import data.Constants;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static io.restassured.config.XmlConfig.xmlConfig;
import static org.hamcrest.Matchers.hasXPath;
import static util.Marshall.marshallSoapRequest;

public class UpdateEmployeeTestSteps {
    String body;
    Response response;
    EmployeeInfo employeeInfo;

    @Step("Prepare employee info for updating")
    public UpdateEmployeeTestSteps prepareEmployeeInfo() {
        ObjectFactory objectFactory = new ObjectFactory();
        employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(22);
        employeeInfo.setName(Constants.UPDATED_NAME);
        employeeInfo.setEmail(Constants.UPDATED_MAIL);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(LocalDate.of(Constants.UPDATED_BIRTH_YEAR, Constants.BIRTH_MONTH, Constants.BIRTH_DAY).toString());
            employeeInfo.setBirthDate(xmlDate);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        employeeInfo.setPhone(Constants.UPDATED_PHONE_NUMBER);
        employeeInfo.setSalary(BigDecimal.valueOf(Constants.UPDATED_SALARY));

        return this;
    }


    @Step("Create update employee request body")
    public UpdateEmployeeTestSteps createUpdateRequestBody() {
        ObjectFactory objectFactory = new ObjectFactory();
        UpdateEmployeeRequest updateEmployeeRequest = objectFactory.createUpdateEmployeeRequest();
        updateEmployeeRequest.setEmployeeInfo(employeeInfo);
        body = marshallSoapRequest(updateEmployeeRequest);
        return this;
    }


    @Step("Send update employee request")
    public UpdateEmployeeTestSteps sendRequest(){
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace("ns2", Constants.NS2_NAME_SPACE)
                        .declareNamespace("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction",  "interfaces.soap.springboot.example.com/exampleSoapHttp/updateEmployeeRequest")
                .body(body)
                .post(Constants.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate response status code is 200")
    public UpdateEmployeeTestSteps validateStatus(){
        Assert.assertEquals(response.getStatusCode(), Constants.OK_STATUS_CODE);
        return this;
    }

    @Step("Validate response message: Content Updated Successfully")
    public UpdateEmployeeTestSteps validateResponse() {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and namespace-uri()='" + Constants.NS2_NAME_SPACE +"']" +
                        "[text()='" + Constants.CONTENT_UPDATED_SUCCESSFULLY + "']"));
        return this;
    }
}

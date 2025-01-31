package steps.EmployeeTest;

import com.example.springboot.soap.interfaces.AddEmployeeRequest;
import com.example.springboot.soap.interfaces.EmployeeInfo;
import com.example.springboot.soap.interfaces.ObjectFactory;
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
import static org.hamcrest.Matchers.*;
import static util.Marshall.marshallSoapRequest;

public class AddEmployee {
    Response response;
    String body;
    EmployeeInfo employeeInfo;


    @Step("Populate EmployeeInfo object with test data")
    public AddEmployee populateEmployeeInfo() {
        ObjectFactory objectFactory = new ObjectFactory();
        employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(Constants.EMPLOYEE_ID);
        employeeInfo.setName(Constants.NAME);
        employeeInfo.setAddress(Constants.ADDRESS);
        employeeInfo.setEmail(Constants.EMAIL);
        employeeInfo.setDepartment(Constants.DEPARTMENT);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(LocalDate.of(Constants.BIRTH_YEAR, Constants.BIRTH_MONTH, Constants.BIRTH_DAY).toString());
            employeeInfo.setBirthDate(xmlDate);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        employeeInfo.setPhone(Constants.PHONE_NUMBER);
        employeeInfo.setSalary(BigDecimal.valueOf(Constants.SALARY));
        return this;
    }



    @Step("Create AddEmployeeRequest SOAP body")
    public AddEmployee createAddRequestBody() {
        ObjectFactory objectFactory = new ObjectFactory();
        AddEmployeeRequest employeeRequest = objectFactory.createAddEmployeeRequest();
        employeeRequest.setEmployeeInfo(employeeInfo);
        body = marshallSoapRequest(employeeRequest);
        return this;
    }



    @Step("Send SOAP request to add employee")
    public AddEmployee sendRequest() {
        response = given()
                //.filter(new AllureRestAssured())
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace("ns2", "http://interfaces.soap.springboot.example.com")
                        .declareNamespace("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "interfaces.soap.springboot.example.com/exampleSoapHttp/addEmployeeRequest")
                .body(body)
                .post(Constants.LOCALHOST_BASE_URI);
        return this;
    }


    @Step("Validate that the HTTP status code is 200")
    public AddEmployee validateStatus() {
        Assert.assertEquals(response.getStatusCode(), Constants.OK_STATUS_CODE);
        return this;
    }


    @Step("Validate SOAP response contains expected success message")
    public AddEmployee validateResponse() {
        response
                .then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and namespace-uri()='" + Constants.NS2_NAME_SPACE + "']" +
                        "[text()='" + Constants.CONTENT_ADDED_SUCCESSFULLY + "']"));
        return this;
    }
}

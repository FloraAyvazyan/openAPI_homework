package SoapUITest;

import data.Constants;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import steps.EmployeeTest.AddEmployee;
import steps.EmployeeTest.DeleteEmployeeSteps;
import steps.EmployeeTest.GetEmployeeByIdSteps;
import steps.EmployeeTest.UpdateEmployeeTestSteps;

@Epic("Employee Management System")
public class EmployeeTest {
    GetEmployeeByIdSteps getEmployeeByIdSteps = new GetEmployeeByIdSteps();
    AddEmployee addEmployee = new AddEmployee();
    UpdateEmployeeTestSteps updateEmployeeTestSteps = new UpdateEmployeeTestSteps();
    DeleteEmployeeSteps deleteEmployeeSteps = new DeleteEmployeeSteps();

    @Test(priority = 1)
    @Feature("Add Employee")
    @Story("As an admin, I want to add an employee")
    @Description("This test adds an employee and checks if the status is 200 and the response is correct")
    @Severity(SeverityLevel.CRITICAL)
    public void addEmployeeTest() {
        addEmployee
                .populateEmployeeInfo()
                .createAddRequestBody()
                .sendRequest()
                .validateStatus()
                .validateResponse();

    }
    @Test(priority = 2)
    @Feature("Get Employee by ID")
    @Story("As an admin, I want to get employee details by ID")
    @Description("This test retrieves employee details by ID and validates the correct values")
    @Severity(SeverityLevel.NORMAL)
    public void getEmployeeByIdTest(){
        getEmployeeByIdSteps
                .getByID(getEmployeeByIdSteps
                        .getMarshalledResponse(Constants.EMPLOYEE_ID));
        getEmployeeByIdSteps
                .validateGetEmployeeByIdResponseID(Constants.EMPLOYEE_ID)
                .validateGetEmployeeByIdResponseName(Constants.NAME)
                .validateGetEmployeeByIdResponseDepartment(Constants.DEPARTMENT)
                .validateGetEmployeeByIdResponsePhone(Constants.PHONE_NUMBER)
                .validateGetEmployeeByIdResponseAddress(Constants.ADDRESS)
                .validateGetEmployeeByIdResponseEmail(Constants.EMAIL);

    }

    @Test(priority = 3)
    @Feature("Update Employee")
    @Story("As an admin, I want to update employee details")
    @Description("This test updates an employee and checks if the changes are reflected correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void updateEmployeeTest() {
        updateEmployeeTestSteps
                .prepareEmployeeInfo()
                .createUpdateRequestBody()
                .sendRequest()
                .validateStatus()
                .validateResponse();
       getEmployeeByIdSteps
                .getByID(getEmployeeByIdSteps
                        .getMarshalledResponse(Constants.EMPLOYEE_ID));
        getEmployeeByIdSteps
                .validateGetEmployeeByIdResponseID(Constants.EMPLOYEE_ID)
                .validateGetEmployeeByIdResponseName(Constants.UPDATED_NAME)
                .validateGetEmployeeByIdResponseEmail(Constants.UPDATED_MAIL);


    }

    @Test(priority = 4)
    @Feature("Delete Employee")
    @Story("As an admin, I want to delete an employee")
    @Description("This test deletes an employee and validates the response status and error message")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteEmployeeTest() {
        deleteEmployeeSteps
                .createDeleteRequestBody(Constants.EMPLOYEE_ID)
                .sendRequest()
                .validateStatus()
                .validateDeleteMessage();
        getEmployeeByIdSteps
                .getByID(getEmployeeByIdSteps
                        .getMarshalledResponse(Constants.EMPLOYEE_ID));
        getEmployeeByIdSteps
                .validateStatus()
                .validateErrorMessage();

    }
}

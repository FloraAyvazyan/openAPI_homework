package mappingHomework;

import config.Employee;
import data.Constants;
import org.testng.annotations.Test;
import steps.EmployeeTest.DeleteEmployeeSteps;
import steps.EmployeeTest.UpdateEmployeeTestSteps;
import java.math.BigDecimal;
import java.util.List;
import static config.DataBaseConfig.dbMapper;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertNotNull;


public class EmployeeTest {
    UpdateEmployeeTestSteps updateEmployeeTestSteps = new UpdateEmployeeTestSteps();
    DeleteEmployeeSteps deleteEmployeeSteps = new DeleteEmployeeSteps();

    @Test
    public void testInsertEmployee3() {
         //**Insert new Employee in DB**:
        dbMapper().count();
            Employee person = new Employee(2, "Flora",
                    "ITC", "123-453-627", "Tbilisi", new BigDecimal(3444), "Fakeemail.com");
            dbMapper().insertEmployee(person);
            dbMapper().count();

        Employee employee = dbMapper().selectById(2);
        System.out.println(employee + "\n");

       //   **Get Employee By ID (`getEmployeeById`)**:
        Employee employee1 = dbMapper().selectById(2);
        assertNotNull(employee1);
        assertEquals(employee1.getName(), "Flora");
        assertEquals(employee1.getDepartment(), "ITC");
        assertEquals(employee1.getPhone(), "123-453-627");
        assertEquals(employee1.getAddress(), "Tbilisi");
        assertEquals(employee1.getEmail(), "Fakeemail.com");


        updateEmployeeTestSteps
                .prepareEmployeeInfo()
                .createUpdateRequestBody()
                .sendRequest()
                .validateStatus()
                .validateResponse();


        Employee updatedEmpWsOAP = dbMapper().selectById(2);
        assertEquals(updatedEmpWsOAP.getName(), "UpdatedWSoap");
        assertEquals(updatedEmpWsOAP.getEmail(), Constants.UPDATED_MAIL);
        assertEquals(updatedEmpWsOAP.getPhone(), Constants.UPDATED_PHONE_NUMBER);
        System.out.println("After update wSOAP");
        System.out.println(updatedEmpWsOAP);


        //---------------------------------------------------------------------
        Employee updatedEmp = dbMapper().selectById(2);
        updatedEmp.setName("UpdatedName");
        updatedEmp.setDepartment("UpdatedDepartment");

        dbMapper().updatePerson(updatedEmp);

        Employee updatedPerson = dbMapper().selectById(2);
        assertEquals(updatedPerson.getName(), "UpdatedName");
        assertEquals(updatedPerson.getDepartment(), "UpdatedDepartment");

        System.out.println("After update with database");
        System.out.println(updatedPerson + "\n");
        //---------------------------------------------------------------------


        deleteEmployeeSteps
                .createDeleteRequestBody(2)
                .sendRequest()
                .validateStatus()
                .validateDeleteMessage();


        List<Employee> AllEmployee = dbMapper().selectAll();
        System.out.println("After deleting");
        System.out.println(AllEmployee);

    }


//    @Test
//    public void testInsertEmployee() {
//        dbMapper().deletePerson(2);
//
//    }

}

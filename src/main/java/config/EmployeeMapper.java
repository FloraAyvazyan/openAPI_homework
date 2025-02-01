package config;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface EmployeeMapper {

        @Insert("INSERT INTO Employee (employee_Id, name, department, phone, address, salary, email) " +
                "VALUES (#{employee_Id}, #{name}, #{department}, #{phone}, #{address}, #{salary}, #{email})")
        void insertEmployee(Employee employee);

    @Select("DELETE FROM Employee WHERE employee_Id = #{employee_Id}")
    void deletePerson(int id);


    @Select("SELECT * FROM Employee WHERE employee_Id = #{employee_Id}")
    Employee selectById(long employeeId);



    @Select("SELECT * FROM Employee")
    List<Employee> selectAll();

    @Select("SELECT * FROM Employee WHERE name = #{name}")
    List<Employee> selectByName(String name);



    @Select("SELECT COUNT(*) FROM Employee")
    int count();


    @Select("UPDATE Employee SET " +
            "name = #{name}, department = #{department}, " +
            "phone = #{phone}, address = #{address}, email = #{email} WHERE employee_Id = #{employee_Id}")
    void updatePerson(Employee employee);

}

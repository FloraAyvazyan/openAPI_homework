package config;

import lombok.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private long employee_Id;
    private String name;
    private String department;
    private String phone;
    private String address;
    private BigDecimal salary;
    private String email;

    public long getEmployeeId() {
        return employee_Id;
    }


}
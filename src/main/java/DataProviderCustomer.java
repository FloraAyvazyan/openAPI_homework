import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class DataProviderCustomer {
    private final Faker faker = new Faker();

    @DataProvider(name = "invalidPasswords")
    public Object[][] provideInvalidPasswords() {
        return new Object[][] {
                {faker.internet().emailAddress(), "1234567"},
                {faker.internet().emailAddress(), "ABCDEFGH"},
                {faker.internet().emailAddress(), "abcdefgh"},
                {faker.internet().emailAddress(), "********"},
                //ვალიდ
                {faker.internet().emailAddress(), "Valid@1234"}
        };
    }
}

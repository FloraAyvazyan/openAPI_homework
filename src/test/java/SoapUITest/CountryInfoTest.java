package SoapUITest;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import steps.CountryTest.CountryInfoSteps;

@Epic("Country Information API")
public class CountryInfoTest {
    CountryInfoSteps countryInfoSteps = new CountryInfoSteps();

    @Test
    @Feature("Validate Continents")
    @Story("As a user, I want to validate the continents data in the response")
    @Description("This test validates various aspects of continent data such as continent count," +
            " continent names, and their corresponding codes.")
    @Severity(SeverityLevel.CRITICAL)
    public void validateContinents() {
        countryInfoSteps
                .sendRequest()
                .validateContinentsCount()
                .extractContinentNames()
                .validateContinentForAN()
                .validateLastContinent()
                .validateUniqueNames()
                .validateContinentsCode()
                .validateContinentsName()
                .validateCodePattern()
                .validateAlphabeticalOrder()
                .validateSixCountries()
                .validateNoNumericNames()
                .validateStartsAndEndsWith();
               // .validateSCodeStartingWithO();

    }


}

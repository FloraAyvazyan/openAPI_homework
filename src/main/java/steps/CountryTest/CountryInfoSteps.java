package steps.CountryTest;

import data.Constants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryInfoSteps {
    private Response response;
    private List<String> sNames;

    @Step("Sending request to fetch the list of continents")
    public CountryInfoSteps sendRequest() {
        response = given()
                .when()
                .get("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName")
                .then()
                .assertThat()
                .statusCode(Constants.OK_STATUS_CODE)
                .extract()
                .response();
        return this;
    }


    @Step("Validating the count of continents")
    public CountryInfoSteps validateContinentsCount() {
        int count = response.xmlPath().getList("ArrayOftContinent.tContinent.sName").size();
        assertThat(count, equalTo(Constants.CONTINENTS_COUNT));
        return this;
    }


    @Step("Extracting the list of continent names")
    public CountryInfoSteps extractContinentNames() {
        sNames = response.xmlPath().getList("ArrayOftContinent.tContinent.sName");
        System.out.println(sNames);
        assertThat(sNames, hasItems(Constants.sNames));
        return this;
    }


    @Step("Validating the continent name for code 'AN'")
    public CountryInfoSteps validateContinentForAN() {
        String continentForAN = response.xmlPath().getString("ArrayOftContinent.tContinent.find { it.sCode == 'AN' }.sName");
        assertThat(continentForAN, equalTo(Constants.ANTARCTICA));
        return this;
    }


    @Step("Validating the last continent in the list")
    public CountryInfoSteps validateLastContinent() {
        String lastContinent = response.xmlPath().getString("ArrayOftContinent.tContinent[-1].sName");
        assertThat(lastContinent, equalTo(Constants.THE_AMERICA));
        return this;
    }

    @Step("Validating that each continent name is unique")
    public CountryInfoSteps validateUniqueNames() {
        assertThat(sNames, hasSize(sNames.stream().distinct().toList().size()));
        return this;
    }


    @Step("Validating the presence of correct continent codes")
    public CountryInfoSteps validateContinentsCode() {
        List<String> continentSCode = response.xmlPath().getList("ArrayOftContinent.tContinent.sCode");
        assertThat(continentSCode, hasItems(Constants.sCodes));
        return this;
    }


    @Step("Validating the presence of correct continent names")
    public CountryInfoSteps validateContinentsName() {
        assertThat(sNames, hasItems(Constants.sNames));
        return this;
    }


    @Step("Validating continent codes follow the pattern of two uppercase letters")
    public CountryInfoSteps validateCodePattern() {
        List<String> codes = response.xmlPath().getList("ArrayOftContinent.tContinent.sCode");
        for (String code : codes) {
            assertThat(code, matchesPattern("[A-Z]{2}"));
        }
        return this;
    }

    @Step("Validating that continent names are sorted alphabetically")
    public CountryInfoSteps validateAlphabeticalOrder() {
        List<String> sortedNames = new ArrayList<>(sNames);
        Collections.sort(sortedNames);
        assertThat(sNames, equalTo(sortedNames));
        return this;
    }

    //   - No Numeric Characters in `sName`
    //   - Validate that no `sName` node contains numeric characters
    //აქ ეს ორი ერთად მაქ იგივეა პრინციპში
    @Step("Validating that no continent name contains numeric characters")
    public CountryInfoSteps validateNoNumericNames() {
        for (String name : sNames) {
            assertThat(name, not(matchesPattern(".*\\d.*")));
        }
        return this;
    }

    @Step("Validating that continent names start with 'A' and end with 'ca'")
    public CountryInfoSteps validateStartsAndEndsWith() {
        response.then().assertThat().body("ListOfContinentsByNameResult.tContinent.sName", hasItems(
                startsWith("A"), endsWith("ca")
        ));
        return this;
    }

    @Step("Validating that the response contains all six continents")
    public CountryInfoSteps validateSixCountries(){
        response.then().assertThat().body("ListOfContinentsByNameResult.tContinent.sName", hasItems(Constants.sNames));
        return this;
    }

    @Step("Validating that the continent code starting with 'O' corresponds to 'Oceania'")
    public CountryInfoSteps validateSCodeStartingWithO() {
        String continentName = response.xmlPath().getString(
                "Envelope.Body.ListOfContinentsByNameResponse.ListOfContinentsByNameResult.tContinent.find { it.sCode.text().startsWith('O') }.sName");
        assertThat(continentName, equalTo(Constants.OCEANIA));
        return this;
    }

}
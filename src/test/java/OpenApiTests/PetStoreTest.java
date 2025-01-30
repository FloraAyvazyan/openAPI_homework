package OpenApiTests;

import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import org.assertj.Assertions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pet.store.v3.invoker.ApiClient;
import pet.store.v3.invoker.JacksonObjectMapper;
import pet.store.v3.model.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static pet.store.v3.invoker.ResponseSpecBuilders.shouldBeCode;
import static pet.store.v3.invoker.ResponseSpecBuilders.validatedWith;

public class PetStoreTest {
    private ApiClient api;
    Faker faker = new Faker();



    @BeforeSuite
    public void createApiClient() {
        api = ApiClient.api(ApiClient.Config.apiConfig()
                .reqSpecSupplier(() -> new RequestSpecBuilder()
                        .log(LogDetail.ALL)
                        .setConfig(config()
                                .objectMapperConfig(objectMapperConfig()

                                        .defaultObjectMapper(JacksonObjectMapper.jackson())))
                        .addFilter(new ErrorLoggingFilter())
                        .addFilter(new AllureRestAssured())
                        .setBaseUri("https://petstore3.swagger.io/api/v3")));
    }

    @Test
    @Feature("Pet Store API")
    @Story("Place a new order for a pet")
    @Severity(io.qameta.allure.SeverityLevel.NORMAL)
    @Description("This test validates the placement of a new pet order with valid data and checks if the order details are correct.")
    public void addNewPet(){
        String dateString = "2022-01-01T00:00:00Z";
        OffsetDateTime shipDate = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);

        Order order = new Order();
        order
                .complete(true)
                .petId(5L)
                .id(2L)
                .quantity(5)
                .shipDate(shipDate)
                .status(Order.StatusEnum.APPROVED);

        validatedWith(shouldBeCode(200));
        api
                .store()
                .placeOrder()
                .body(order)
                .execute(response -> response);

        Assertions.assertThat(order)
                .hasId(2L)
                .hasPetId(5L)
                .hasQuantity(5)
                .hasShipDate(shipDate)
                .hasStatus(Order.StatusEnum.APPROVED);

    }


    @Test
    @Story("Add New Pet to Pet Store")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Pet Management")
    @Description("This test case validates the ability to add a new " +
            "pet to the pet store with the necessary details, such as ID," +
            " name, status, tags, photo URLs, and category.")
    public void testCase2(){
        String name = faker.animal().name();
        Pet pet = new Pet();
         pet
                .id(5L)
                .name(name)
                .status(Pet.StatusEnum.AVAILABLE)
                .addPhotoUrlsItem("string")
                .addTagsItem(new Tag()
                        .id(0L)
                        .name("string"))
                .category(new Category()
                        .id(0L)
                        .name("string"));

        api
                .pet()
                .addPet()
                .body(pet)
                .execute(res -> res);
        validatedWith(shouldBeCode(200));
        Assertions.assertThat(pet)
                .hasId(5L)
                .hasName(name)
                .hasStatus(Pet.StatusEnum.AVAILABLE)
                .hasPhotoUrls("string")
                .hasTags(new Tag().id(0L).name("string"))
                .hasCategory(new Category().id(0L).name("string"));

    }









}

package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Courier;

import static ru.yandex.practicum.config.RestConfig.*;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps {

    @Step("Create a courier")
    public ValidatableResponse createACourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Log in as courier")
    public ValidatableResponse logInAsCourier(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then();
    }

    public Integer getCourierId(Courier courier) {
        return logInAsCourier(courier)
                .extract()
                .jsonPath()
                .getObject("id", Integer.class);
    }
    @Step("Delete the created courier")
    public ValidatableResponse deleteCourier(Courier courier) {
            return given()
                    .when()
                    .delete(COURIER + courier.getCourierId())
                    .then();
    }

    @Step("Assert that the courier is created")
    public void assertThatCourierCreated(ValidatableResponse response) {
        response
                .statusCode(HTTP_CREATED)
                .body("ok", is(true));
    }

    @Step("Assert that the courier is not created")
    public void assertThatCourierNotCreated(ValidatableResponse response, int responseCode, String responseBody) {
        response
                .statusCode(responseCode)
                .body("message", is(responseBody));
    }

    @Step("Assert that the courier is logged in")
    public void assertThatCourierLoggedIn(ValidatableResponse response) {
        response
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }

    @Step("Assert that the courier is not logged in")
    public void assertThatCourierIsNotLoggedIn(ValidatableResponse response, int responseCode, String responseBody) {
        response
                .statusCode(responseCode)
                .body("message", is(responseBody));
    }


}

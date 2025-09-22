package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Courier;
import ru.yandex.practicum.steps.CourierSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;

public class CourierLoginTest extends BaseTest {

    Courier courier = new Courier("couriersamokata1", "qwerty123");
    CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        courierSteps.createACourier(courier).statusCode(HTTP_CREATED);
    }

    @Test
    @DisplayName("Login returns 200 OK with valid login and password")
    @Description("Verify that a courier can log in using valid credentials.\nExpected result: HTTP 200 response and the courier ID in the response body.")
    public void loginCourierWithValidLoginAndPasswordShouldReturn200() {
        ValidatableResponse response = courierSteps.logInAsCourier(courier);

        courierSteps.assertThatCourierLoggedIn(response);
    }

    @Test
    @DisplayName("Should return 400 when login field is missing")
    @Description("Verify that a courier cannot log in when the login field is missing.\nExpected result: HTTP 400 response and an error message \"Недостаточно данных для входа\" in response body.")
    public void loginCourierWithoutLoginShouldReturn400() {
        Courier incorrectCourier = new Courier()
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps
                .logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return 400 when password field is missing")
    @Description("Verify that a courier cannot log in when the password field is missing.\nExpected result: HTTP 400 response and an error message \"Недостаточно данных для входа\" in response body.")
    public void loginCourierWithoutPasswordShouldReturn400() {
        Courier incorrectCourier = new Courier()
                .setLogin("couriersamokata1");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return 404 when logging in as a non-existent user")
    @Description("Verify that a courier cannot log in using credentials of a non-existent user.\nExpected result: HTTP 404 response and an error message \"Учетная запись не найдена\" in response body.")
    public void loginCourierWithNonExistentUserShouldReturn404() {
        Courier incorrectCourier = new Courier()
                .setLogin("customer")
                .setPassword("Pass_321!");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Should return 404 when login is incorrect")
    @Description("Verify that a courier cannot log in using incorrect login.\nExpected result: HTTP 404 response and an error message \"Учетная запись не найдена\" in response body.")
    public void loginCourierWithIncorrectLoginShouldReturn404() {
        Courier incorrectCourier = new Courier()
                .setLogin("curirsomokata1")
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Should return 404 when password is incorrect")
    @Description("Verify that a courier cannot log in using incorrect password.\nExpected result: HTTP 404 response and an error message \"Учетная запись не найдена\" in response body.")
    public void loginCourierWithIncorrectPasswordShouldReturn404() {
        Courier incorrectCourier = new Courier()
                .setLogin("couriersamokata1")
                .setPassword("qwer321!");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @After
    public void cleanUp() {
        courier.setCourierId(
                courierSteps.getCourierId(courier));

        courierSteps
                .deleteCourier(courier).statusCode(HTTP_OK);

    }
}

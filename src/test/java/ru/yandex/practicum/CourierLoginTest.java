package ru.yandex.practicum;

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
    public void loginCourier_WithValidLoginAndPassword_ShouldReturn200() {
        ValidatableResponse response = courierSteps.logInAsCourier(courier);

        courierSteps.assertThatCourierLoggedIn(response);
    }

    @Test
    @DisplayName("Should return 400 when login field is missing")
    public void loginCourier_WithoutLogin_ShouldReturn400() {
        Courier incorrectCourier = new Courier()
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps
                .logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return 400 when password field is missing")
    public void loginCourier_WithoutPassword_ShouldReturn400() {
        Courier incorrectCourier = new Courier()
                .setLogin("couriersamokata1");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Should return 404 when logging in as a non-existent user")
    public void loginCourier_WithNonExistentUser_ShouldReturn404() {
        Courier incorrectCourier = new Courier()
                .setLogin("customer")
                .setPassword("Pass_321!");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Should return 404 when login is incorrect")
    public void loginCourier_WithIncorrectLogin_ShouldReturn404() {
        Courier incorrectCourier = new Courier()
                .setLogin("curirsomokata1")
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps.logInAsCourier(incorrectCourier);

        courierSteps.assertThatCourierIsNotLoggedIn(
                response, HTTP_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Should return 404 when password is incorrect")
    public void loginCourier_WithIncorrectPassword_ShouldReturn404() {
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

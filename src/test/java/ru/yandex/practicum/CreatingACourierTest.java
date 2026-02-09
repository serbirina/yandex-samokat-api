package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.steps.CourierSteps;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.models.Courier;

import static java.net.HttpURLConnection.*;

public class CreatingACourierTest extends BaseTest {
    CourierSteps courierSteps = new CourierSteps();
    Courier courier = new Courier();

    @Test
    @DisplayName("Should return 201 when creating a courier with valid required fields")
    @Description("Verify that a courier can be created with valid required fields.\nExpected result: HTTP 201 response and \"ok: true\" in response body.")
    public void createCourierWithValidRequiredFieldsShouldReturn201() {
        courier
                .setLogin("couriersamokata1")
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps
                .createACourier(courier);

        courierSteps.assertThatCourierCreated(response);

    }

    @Test
    @DisplayName("Should return 400 when creating a courier without login")
    @Description("Verify that creating a courier without a login fails.\nExpected result: HTTP 400 response and an error message \"Недостаточно данных для создания учетной записи\" in response body.")
    public void createCourierWithoutLoginShouldReturn400() {
        courier
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps.createACourier(courier);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should return 400 when creating a courier without password")
    @Description("Verify that creating a courier without a password fails.\nExpected result: HTTP 400 response and an error message \"Недостаточно данных для создания учетной записи\" in response body.")
    public void createCourierWithoutPasswordShouldReturn400() {
        courier
                .setLogin("couriersamokata1");

        ValidatableResponse response = courierSteps.createACourier(courier);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should return 409 when creating a courier with an already used login")
    @Description("Verify that creating a courier with an existing login fails.\nExpected result: HTTP 409 response and an error message \"Этот логин уже используется. Попробуйте другой.\" in response body.")
    public void createCourierWithExistingLoginShouldReturn409() {
        String login = "couriersamokata1";
        courier
                .setLogin(login)
                .setPassword("qwerty123");

        courierSteps
                .createACourier(courier);

        Courier courierWithSameLogin = new Courier();

        courierWithSameLogin
                .setLogin(login)
                .setPassword("qwerty321");

        ValidatableResponse response = courierSteps.createACourier(courierWithSameLogin);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Should return 409 when trying to create two identical couriers")
    @Description("Verify that creating a courier twice with the same data fails.\nExpected result: HTTP 409 response and an error message \"Этот логин уже используется. Попробуйте другой.\" in response body.")
    public void createCourierTwiceWithSameDataShouldReturn409() {
        courier
                .setLogin("couriersamokata1")
                .setPassword("qwerty123");

        courierSteps
                .createACourier(courier);

        ValidatableResponse response = courierSteps.createACourier(courier);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @After
    public void cleanUp() {
        if (courier.getLogin() == null || courier.getPassword() == null) {
            return;
        }
        ValidatableResponse response = courierSteps.logInAsCourier(courier);

        if (response.extract().statusCode() == HTTP_OK) {
            int courierId = courierSteps.getCourierId(courier);
            courier.setCourierId(courierId);
            courierSteps.deleteCourier(courier);
        }
    }
}

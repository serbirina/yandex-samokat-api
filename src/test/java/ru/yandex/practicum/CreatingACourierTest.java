package ru.yandex.practicum;

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
    public void createCourier_WithValidRequiredFields_ShouldReturn201() {
        courier
                .setLogin("couriersamokata1")
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps
                .createACourier(courier);

        courierSteps.assertThatCourierCreated(response);

    }

    @Test
    @DisplayName("Should return 400 when creating a courier without login")
    public void createCourier_WithoutLogin_ShouldReturn400() {
        courier
                .setPassword("qwerty123");

        ValidatableResponse response = courierSteps.createACourier(courier);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should return 400 when creating a courier without password")
    public void createCourier_WithoutPassword_ShouldReturn400() {
        courier
                .setLogin("couriersamokata1");

        ValidatableResponse response = courierSteps.createACourier(courier);

        courierSteps.assertThatCourierNotCreated(
                response, HTTP_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Should return 409 when creating a courier with an already used login")
    public void createCourier_WithExistingLogin_ShouldReturn409() {
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
    public void createCourier_TwiceWithSameData_ShouldReturn409() {
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

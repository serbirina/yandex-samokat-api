package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.TrackOrder;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.yandex.practicum.config.RestConfig.*;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.*;

public class OrderSteps {

    @Step("Create an order")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Send request to get list of orders")
    public ValidatableResponse getListOfOrders() {
        return given()
                .when()
                .get(ORDERS)
                .then();
    }

    @Step("Cancel the order")
    public ValidatableResponse cancelOrder(TrackOrder trackOrder) {
        return given()
                .contentType(ContentType.JSON)
                .body(trackOrder)
                .when()
                .queryParam("track", trackOrder.getTrack())
                .put(CANCEL_ORDER)
                .then();
    }

    @Step("Assert that an order was created")
    public void assertThatOrderCreated(ValidatableResponse response) {
        response
                .statusCode(HTTP_CREATED)
                .body("track", notNullValue());
    }

    @Step("Assert that the order contain list of order")
    public void assertThatOrderContainListOfOrder(ValidatableResponse response) {
        response.statusCode(HTTP_OK)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0))
                .body("orders[0]", instanceOf(Map.class));
    }
}

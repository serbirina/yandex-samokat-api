package ru.yandex.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.TrackOrder;
import ru.yandex.practicum.steps.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@RunWith(Parameterized.class)
public class CreateOrderWithVariousColorOptionsTest extends BaseTest {

    private final OrderSteps orderSteps = new OrderSteps();
    private final Order order = new Order();

    private final List<String> color;
    private ValidatableResponse response;

    public CreateOrderWithVariousColorOptionsTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Test data: scooter color - {0}")
    public static Object[][] getParameters() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        order
            .setFirstName("Лев")
            .setLastName("Толстой")
            .setAddress("М. Пироговская, 25")
            .setMetroStation("15")
            .setPhone("81002223311")
            .setRentTime(1)
            .setDeliveryDate("2025-10-25T21:00:00.000Z");
    }

    @Test
    @DisplayName("Order creation returns 201 Created with various color combinations")
    @Description("Verify that an order can be created with black, grey, both colors, or with no color selected.\nExpected result: HTTP 201 response and the order tracking number in the response body.")
    public void createOrderWithVariousColorOptionsShouldReturn201() {
        order.setColor(color);

        response = orderSteps.createOrder(order);

        orderSteps.assertThatOrderCreated(response);
    }

    @After
    public void cleanUp() {
        int trackId = response.extract().jsonPath().getInt("track");
        TrackOrder trackOrder = new TrackOrder();
        trackOrder.setTrack(trackId);

        orderSteps
                .cancelOrder(trackOrder)
                .statusCode(HTTP_OK);
    }
}

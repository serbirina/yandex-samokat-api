package ru.yandex.practicum;

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
    private final TrackOrder trackOrder = new TrackOrder();

    private final List<String> color;

    public CreateOrderWithVariousColorOptionsTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
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
    public void createOrder_WithVariousColorOptions_ShouldReturn201() {
        order.setColor(color);

        ValidatableResponse response = orderSteps.createOrder(order);

        orderSteps.assertThatOrderCreated(response);

        int trackId = response.extract().jsonPath().getInt("track");
        trackOrder.setTrack(trackId);
    }

    @After
    public void cleanUp() {
        orderSteps
                .cancelOrder(trackOrder)
                .statusCode(HTTP_OK);
    }
}

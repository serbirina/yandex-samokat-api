package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.TrackOrder;
import ru.yandex.practicum.steps.OrderSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class GetOrderListTest extends BaseTest {
    private final OrderSteps orderSteps = new OrderSteps();
    private final Order order = new Order();
    private final TrackOrder trackOrder = new TrackOrder();

    @Before
    public void setUp() {
        order
                .setFirstName("Лев")
                .setLastName("Толстой")
                .setAddress("М. Пироговская, 25")
                .setMetroStation("15")
                .setPhone("81002223311")
                .setRentTime(1)
                .setDeliveryDate("2025-10-25T21:00:00.000Z")
                .setColor(List.of("BLACK"));

        ValidatableResponse response = orderSteps.createOrder(order);

        int trackId = response.extract().jsonPath().getInt("track");
        trackOrder.setTrack(trackId);
    }

    @Test
    @DisplayName("Getting list of orders returns 200 and contains a list of orders in response body")
    public void getListOfOrders_WithValidRequest_ShouldReturn200AndOrderList() {
        ValidatableResponse response = orderSteps.getListOfOrders();

        orderSteps.assertThatOrderContainListOfOrder(response);
    }

    @After
    public void cleanUp() {
        orderSteps
                .cancelOrder(trackOrder)
                .statusCode(HTTP_OK);
    }
}

package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    final private OrderRepository orderRepository = mock(OrderRepository.class);
    final private UserRepository userRepository = mock(UserRepository.class);
    final private UserOrder userOrder = mock(UserOrder.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit(){
        User user = new User();
        user.setUsername("test");
        user.setId(1L);

        Cart cart = new Cart();
        cart.setId(2L);
        cart.setUser(user);
        cart.setItems(new ArrayList<Item>());

        user.setCart(cart);

        Item item = new Item();
        item.setId(3L);
        item.setPrice(new BigDecimal(200.00));
        item.setName("Doll");
        item.setDescription("Pretty Doll");
        cart.getItems().add(item);

        UserOrder order = new UserOrder();
        order.setUser(user);
        order.setTotal(new BigDecimal(200.00));
        order.setItems(cart.getItems());

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getUser());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user.getUsername(), response.getBody().getUser().getUsername());
    }

    @Test
    public void getOrdersForUser() {
        User user = new User();
        user.setUsername("test");
        user.setId(1L);

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setTotal(new BigDecimal(200.00));
        userOrder.setId(2L);
        userOrders.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userOrders.get(0).getId(), response.getBody().get(0).getId());
    }

    @Test
    public void getOrdersForUserNegPath() {
        User user = new User();
        user.setUsername("test");
        user.setId(1L);

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setTotal(new BigDecimal(200.00));
        userOrder.setId(2L);
        userOrders.add(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

}

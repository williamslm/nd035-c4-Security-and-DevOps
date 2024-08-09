package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    final private CartRepository cartRepository = mock(CartRepository.class);
    final private UserRepository userRepository = mock(UserRepository.class);
    final private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
    }

    @Test
    public void addToCart(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");

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

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item));
        when(cartRepository.save(cart)).thenReturn(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(2L);
        request.setQuantity(3);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cart, response.getBody());
        assertEquals(cart.getId(), response.getBody().getId());
    }

    @Test
    public void addToCartNegPath(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");

        Cart cart = new Cart();
        cart.setId(2L);
        cart.setUser(user);
        cart.setItems(new ArrayList<Item>());

        user.setCart(cart);

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(itemRepository.findById(2L)).thenReturn(Optional.empty());
        when(cartRepository.save(cart)).thenReturn(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(2L);
        request.setQuantity(3);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void removeFromCart(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");

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

        Item item2 = new Item();
        item2.setId(4L);
        item2.setPrice(new BigDecimal(50.00));
        item2.setName("Ball");
        item2.setDescription("Basketball");
        cart.getItems().add(item2);

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(itemRepository.findById(3L)).thenReturn(Optional.of(item));
        when(cartRepository.save(cart)).thenReturn(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(3L);
        request.setQuantity(3);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getItems().size());
        assertEquals(item2.getId(), response.getBody().getItems().get(0).getId());

    }


}

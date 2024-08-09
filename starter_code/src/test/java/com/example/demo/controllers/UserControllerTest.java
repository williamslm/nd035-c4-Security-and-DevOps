package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("passwordhashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("passwordhashed", user.getPassword());
    }

    @Test
    public void passwords_dont_match(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("testPassword");
        userRequest.setConfirmPassword("testPassword2");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void retrieve_user_happy_path() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setId(0);
        when(userRepository.findByUsername("test")).thenReturn(user);
        final ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        User user2 = response.getBody();
        assertEquals(user.getId(), user2.getId());
        assertEquals(user.getUsername(), user2.getUsername());
    }

    @Test
    public void retrieve_user_not_so_happy_path() throws Exception {
        User user = null;
        when(userRepository.findByUsername("test")).thenReturn(user);
        final ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        User user2 = response.getBody();
        assertNull(user2);
    }

    @Test
    public void retrieve_id_happy_path() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setId(0);
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        User user2 = response.getBody();
        assertEquals(user.getId(), user2.getId());
        assertEquals(user.getUsername(), user2.getUsername());
    }

    @Test
    public void retrieve_id_not_so_happy_path() throws Exception {
        User user = null;

        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}


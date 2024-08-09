package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems() {
        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setName("Doll");
        item.setDescription("Pretty doll");
        item.setPrice(new BigDecimal("100.00"));
        itemList.add(item);
        item.setId(2L);
        item.setName("Pizza");
        item.setDescription("Pretty pizza");
        item.setPrice(new BigDecimal("50.00"));
        itemList.add(item);

        when(itemRepository.findAll()).thenReturn(itemList);
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(itemList, response.getBody());
        assertEquals(itemList.size(), response.getBody().size());
    }

    @Test
    public void getItemById() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Doll");
        item.setDescription("Pretty doll");
        item.setPrice(new BigDecimal("100.00"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(item, response.getBody());
        assertEquals(item.getId(), response.getBody().getId());
        assertEquals(item.getName(), response.getBody().getName());
    }

    @Test
    public void getItemByIdNegPath() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void getItemByName() {
        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setName("Doll");
        item.setDescription("Pretty doll");
        item.setPrice(new BigDecimal("100.00"));
        itemList.add(item);

        when(itemRepository.findByName("Doll")).thenReturn(itemList);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Doll");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(itemList, response.getBody());
        assertEquals(itemList.size(), response.getBody().size());
    }

    @Test
    public void getItemByNameNegPath() {
        List<Item> itemList = new ArrayList<>();

        when(itemRepository.findByName("Doll")).thenReturn(itemList);
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Doll");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}

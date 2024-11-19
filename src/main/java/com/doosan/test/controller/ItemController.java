package com.doosan.test.controller;


import com.doosan.test.dto.ItemRequestDto;
import com.doosan.test.dto.ItemResponseDto;
import com.doosan.test.dto.ItemResponseEditDto;
import com.doosan.test.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService ItemService) {
        this.itemService =  ItemService;
    }

    @PostMapping("/items")
    public ItemResponseDto createItem(@RequestBody ItemRequestDto requestDto) {
        return itemService.createItem(requestDto);
    }

    @GetMapping("/items")
    public List<ItemResponseDto> getItems() {
        return itemService.getItems();
    }

    @PutMapping("/items/{id}")
    public ItemResponseEditDto updateItem(@PathVariable Long id, @RequestBody ItemRequestDto requestDto) {
        return itemService.updateItem(id, requestDto);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();
        response.put("msg", "삭제완료");

        return ResponseEntity.ok(response);
    }




}

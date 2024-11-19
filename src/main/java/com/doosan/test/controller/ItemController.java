package com.doosan.test.controller;

import com.doosan.test.dto.ItemRequestDto;
import com.doosan.test.dto.ItemResponseDto;
import com.doosan.test.dto.ItemResponseEditDto;
import com.doosan.test.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
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

        Map<String, String> response;

        try {
            response = itemService.deleteItem(id);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("아이템 아이디 : {} 찾을 수 없음: {}", id, e.getMessage());
            response = new HashMap<>();
            response.put("msg", "삭제 실패: 선택한 내용이 없음");

            return ResponseEntity.status(404).body(response);

        } catch (Exception e) {
            logger.error("삭제 중 오류 id {}: {}", id, e.getMessage());
            response = new HashMap<>();
            response.put("msg", "삭제 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

    }
}
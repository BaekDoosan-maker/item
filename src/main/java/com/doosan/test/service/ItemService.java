package com.doosan.test.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doosan.test.dto.ItemRequestDto;
import com.doosan.test.dto.ItemResponseDto;
import com.doosan.test.dto.ItemResponseEditDto;
import com.doosan.test.entity.Item;

import com.doosan.test.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);


    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponseDto createItem(ItemRequestDto requestDto) {
        Item item = new Item(requestDto);
        Item saveItem = itemRepository.save(item);
        ItemResponseDto itemResponseDto = new ItemResponseDto(item);

        return itemResponseDto;
    }

    public List<ItemResponseDto> getItems() {
        return itemRepository.findAllByOrderByIdDesc().stream().map(ItemResponseDto::new).toList();

    }

    public List<ItemResponseDto> getItemsByKeyword(String keyword) {
        System.out.println("getItemsByKeyword" + keyword);
        return itemRepository.findAllByContentContainingOrderByIdDesc(keyword).stream().map(ItemResponseDto::new).toList();
    }

    @Transactional
    public ItemResponseEditDto updateItem(Long id, ItemRequestDto requestDto) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Item not found")
        );
        item.update(requestDto);
        Item updatedItem = itemRepository.save(item);
        return new ItemResponseEditDto(updatedItem);
    }

    @Transactional
    public Map<String, String> deleteItem(Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Item item = findItem(id);
            itemRepository.delete(item);
            logger.info(" id : {} 정상 삭제 됨.", id);
            response.put("msg", "삭제 완료");

        } catch (Exception e) {
            logger.error("id 삭제오류 {}: {}", id, e.getMessage());
            throw new RuntimeException("id 삭제 실패 " + id, e);
        }
        return response;
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 내용이 존재하지 않습니다.")
        );
    }

}


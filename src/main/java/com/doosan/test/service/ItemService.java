package com.doosan.test.service;


import com.doosan.test.dto.ItemRequestDto;
import com.doosan.test.dto.ItemResponseDto;
import com.doosan.test.dto.ItemResponseEditDto;
import com.doosan.test.entity.Item;

import com.doosan.test.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

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

    public Long deleteItem(Long id) {
        Item item = findItem(id);
        itemRepository.delete(item);
        return id;

    }

    private Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 내용이 존재하지 않습니다.")
        );
    }

}


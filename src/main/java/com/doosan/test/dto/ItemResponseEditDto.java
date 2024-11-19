package com.doosan.test.dto;

import com.doosan.test.entity.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
@Getter
public class ItemResponseEditDto {
    private Long id;
    private String title;
    private String content;
    private int price;
    private String username;

    public ItemResponseEditDto(Item Item) {
        this.id = Item.getId();
        this.title = Item.getTitle();
        this.content = Item.getContent();
        this.price = Item.getPrice();
        this.username = Item.getUsername();

    }

}

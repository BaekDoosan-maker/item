package com.doosan.test.entity;

import com.doosan.test.dto.ItemRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Item")
@NoArgsConstructor
public class Item  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "username", nullable = false)
    private String username;



    public Item(ItemRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.content = requestDto.getContent();
        this.price = Integer.parseInt(requestDto.getPrice());
    }

    public void update(ItemRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.content = requestDto.getContent();
        this.price = Integer.parseInt(requestDto.getPrice());
    }
}
package com.doosan.test.repository;

import com.doosan.test.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOrderByIdDesc();
    List<Item> findAllByContentContainingOrderByIdDesc(String keyword);
}
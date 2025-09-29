package com.lanchonete.lanchon.models.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lanchonete.lanchon.models.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> { }

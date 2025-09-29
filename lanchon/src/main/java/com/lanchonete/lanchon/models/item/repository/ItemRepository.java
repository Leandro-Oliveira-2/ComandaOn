package com.lanchonete.lanchon.models.item.repository;

import com.lanchonete.lanchon.models.item.entity.Item;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    List<Item> findAllById(@NotNull List<Long> longs);
}

package com.lanchonete.lanchon.models.item.controller;

import com.lanchonete.lanchon.models.item.dto.CreateItem;
import com.lanchonete.lanchon.models.item.dto.ItemDTO;
import com.lanchonete.lanchon.models.item.dto.UpdateItem;
import com.lanchonete.lanchon.models.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDTO> listItemsByOrder(@PathVariable Long orderId) {
        return itemService.findAllByOrder(orderId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return itemService.getItem(orderId, itemId);
    }

    @PostMapping
    public ItemDTO addItem(@PathVariable Long orderId, @RequestBody CreateItem dto) {
        return itemService.addItemToOrder(orderId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO updateItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody UpdateItem dto) {
        return itemService.updateItem(orderId, itemId, dto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        itemService.removeItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }

    // Opcional: rota admin/debug
    @GetMapping("/all")
    public List<ItemDTO> listAll() {
        return itemService.findAll();
    }
}

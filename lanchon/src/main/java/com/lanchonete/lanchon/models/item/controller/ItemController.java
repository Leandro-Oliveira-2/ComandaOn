package com.lanchonete.lanchon.models.item.controller;

import com.lanchonete.lanchon.models.item.dto.CreateItem;
import com.lanchonete.lanchon.models.item.dto.ItemDTO;
import com.lanchonete.lanchon.models.item.dto.UpdateItem;
import com.lanchonete.lanchon.models.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item nao localizado para o pedido")
    })
    @GetMapping("/{itemId}")
    public ItemDTO getItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return itemService.getItem(orderId, itemId);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item criado"),
            @ApiResponse(responseCode = "404", description = "Pedido ou produto nao encontrados")
    })
    @PostMapping
    public ItemDTO addItem(@PathVariable Long orderId, @RequestBody @Valid CreateItem dto) {
        return itemService.addItemToOrder(orderId, dto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado"),
            @ApiResponse(responseCode = "404", description = "Item nao localizado para o pedido")
    })
    @PatchMapping("/{itemId}")
    public ItemDTO updateItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody UpdateItem dto) {
        return itemService.updateItem(orderId, itemId, dto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removido"),
            @ApiResponse(responseCode = "404", description = "Item nao localizado para o pedido")
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        itemService.removeItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<ItemDTO> listAll() {
        return itemService.findAll();
    }
}

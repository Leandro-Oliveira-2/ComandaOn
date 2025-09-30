package com.lanchonete.lanchon.models.product.controller;

import com.lanchonete.lanchon.models.product.dto.CreateProductDTO;
import com.lanchonete.lanchon.models.product.dto.ProductResponseDTO;
import com.lanchonete.lanchon.models.product.dto.UpdateProductDTO;
import com.lanchonete.lanchon.models.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid CreateProductDTO dto) {
        ProductResponseDTO created = productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<ProductResponseDTO> listAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PatchMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @RequestBody UpdateProductDTO dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

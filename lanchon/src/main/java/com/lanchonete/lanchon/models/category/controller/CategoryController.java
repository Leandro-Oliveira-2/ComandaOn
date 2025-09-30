package com.lanchonete.lanchon.models.category.controller;

import com.lanchonete.lanchon.models.category.dto.CategoryResponseDTO;
import com.lanchonete.lanchon.models.category.dto.CreateCategoryDTO;
import com.lanchonete.lanchon.models.category.dto.UpdateCategoryDTO;
import com.lanchonete.lanchon.models.category.service.CategoryService;
import com.lanchonete.lanchon.models.product.dto.ProductResponseDTO;
import com.lanchonete.lanchon.models.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        CategoryResponseDTO category = categoryService.create(createCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        CategoryResponseDTO category = categoryService.update(id, updateCategoryDTO);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByCategoryId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}

package com.lanchonete.lanchon.models.category.controller;

import com.lanchonete.lanchon.models.category.dto.CreateCategoryDTO;
import com.lanchonete.lanchon.models.category.dto.UpdateCategoryDTO;
import com.lanchonete.lanchon.models.category.entity.Category;
import com.lanchonete.lanchon.models.category.service.CategoryService;
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

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        Category category = categoryService.create(createCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        Category category = categoryService.update(id, updateCategoryDTO);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
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
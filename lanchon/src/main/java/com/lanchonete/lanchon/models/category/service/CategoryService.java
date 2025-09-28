package com.lanchonete.lanchon.models.category.service;

import com.lanchonete.lanchon.models.category.dto.CreateCategoryDTO;
import com.lanchonete.lanchon.models.category.dto.UpdateCategoryDTO;
import com.lanchonete.lanchon.models.category.entity.Category;
import com.lanchonete.lanchon.models.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CreateCategoryDTO createCategoryDTO) {
        Category category = new Category();
        category.setName(createCategoryDTO.name());
        category.setSort_order(createCategoryDTO.sortOrder());
        category.setActive(createCategoryDTO.active());
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public Category update(Long id, UpdateCategoryDTO updateCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));

        if (updateCategoryDTO.name() != null) {
            category.setName(updateCategoryDTO.name());
        }
        if (updateCategoryDTO.sortOrder() != null) {
            category.setSort_order(updateCategoryDTO.sortOrder());
        }
        if (updateCategoryDTO.active() != null) {
            category.setActive(updateCategoryDTO.active());
        }

        return categoryRepository.save(category);
    }
}
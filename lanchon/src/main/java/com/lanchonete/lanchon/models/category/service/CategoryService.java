package com.lanchonete.lanchon.models.category.service;

import com.lanchonete.lanchon.models.category.dto.CategoryResponseDTO;
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

    public CategoryResponseDTO create(CreateCategoryDTO createCategoryDTO) {
        Category category = new Category();
        category.setName(createCategoryDTO.name());
        category.setSort_order(createCategoryDTO.sortOrder());
        category.setActive(createCategoryDTO.active());
        return toDto(categoryRepository.save(category));
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryService::toDto)
                .toList();
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
        return toDto(category);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponseDTO update(Long id, UpdateCategoryDTO updateCategoryDTO) {
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

        return toDto(categoryRepository.save(category));
    }

    private static CategoryResponseDTO toDto(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getSort_order(),
                category.getActive()
        );
    }
}

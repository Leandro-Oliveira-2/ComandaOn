package com.lanchonete.lanchon.models.product.service;

import com.lanchonete.lanchon.exception.domain.CategoryNotFoundException;
import com.lanchonete.lanchon.exception.domain.InvalidPayloadException;
import com.lanchonete.lanchon.exception.domain.ProductNotFoundException;
import com.lanchonete.lanchon.models.category.entity.Category;
import com.lanchonete.lanchon.models.category.repository.CategoryRepository;
import com.lanchonete.lanchon.models.product.dto.CreateProductDTO;
import com.lanchonete.lanchon.models.product.dto.ProductResponseDTO;
import com.lanchonete.lanchon.models.product.dto.UpdateProductDTO;
import com.lanchonete.lanchon.models.product.entity.Product;
import com.lanchonete.lanchon.models.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponseDTO create(CreateProductDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));

        Product product = new Product();
        product.setCategory(category);
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setActive(dto.active());

        return toDto(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(ProductService::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return productRepository.findById(Math.toIntExact(id))
                .map(ProductService::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        return productRepository.findByCategory_Id(categoryId).stream()
                .map(ProductService::toDto)
                .toList();
    }

    @Transactional
    public ProductResponseDTO update(Long id, UpdateProductDTO dto) {
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (dto.categoryId() != null) {
            Category category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));
            product.setCategory(category);
        }
        if (dto.name() != null) {
            product.setName(dto.name());
        }
        if (dto.description() != null) {
            product.setDescription(dto.description());
        }
        if (dto.price() != null) {
            if (dto.price() < 0) {
                throw new InvalidPayloadException("O preco do produto deve ser maior ou igual a zero");
            }
            product.setPrice(dto.price());
        }
        if (dto.active() != null) {
            product.setActive(dto.active());
        }

        return toDto(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    private static ProductResponseDTO toDto(Product product) {
        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;

        return new ProductResponseDTO(
                (long) product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getActive(),
                categoryId,
                categoryName
        );
    }
}

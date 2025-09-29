package com.lanchonete.lanchon.models.product.service;

import com.lanchonete.lanchon.models.category.entity.Category;
import com.lanchonete.lanchon.models.category.repository.CategoryRepository;
import com.lanchonete.lanchon.models.product.dto.UpdateProductDTO;
import com.lanchonete.lanchon.models.product.entity.Product;
import com.lanchonete.lanchon.models.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public UpdateProductDTO updateProduct(UpdateProductDTO productDTO, Long id) {
        int productId = Math.toIntExact(id);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));

        if (productDTO.categoryId() != null) {
            Category category = categoryRepository.findById(productDTO.categoryId().longValue())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + productDTO.categoryId()));
            product.setCategory(category);
        }

        if (productDTO.name() != null) {
            product.setName(productDTO.name());
        }
        if (productDTO.description() != null) {
            product.setDescription(productDTO.description());
        }

        if (productDTO.price() < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        product.setPrice(productDTO.price());

        if (productDTO.active() != null) {
            product.setActive(productDTO.active());
        }

        Product savedProduct = productRepository.save(product);

        Integer categoryId = null;
        String categoryName = null;
        if (savedProduct.getCategory() != null) {
            categoryId = Math.toIntExact(savedProduct.getCategory().getId());
            categoryName = savedProduct.getCategory().getName();
        }

        return new UpdateProductDTO(
                savedProduct.getId(),
                categoryId,
                categoryName,
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getActive()
        );
    }
}

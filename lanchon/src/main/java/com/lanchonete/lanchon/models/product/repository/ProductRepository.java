package com.lanchonete.lanchon.models.product.repository;

import com.lanchonete.lanchon.models.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}

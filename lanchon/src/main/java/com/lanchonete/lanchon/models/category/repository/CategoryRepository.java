package com.lanchonete.lanchon.models.category.repository;

import com.lanchonete.lanchon.models.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

}

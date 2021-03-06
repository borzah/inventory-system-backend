package com.demo.inventory.item.repository;

import com.demo.inventory.item.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryIdAndUserId(Long categoryId, Long userId);

    Optional<Category> findByUserIdAndCategoryName(Long userId, String categoryName);

    List<Category> findAllByUserId(Long userId);
}

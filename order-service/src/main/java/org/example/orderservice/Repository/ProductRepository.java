package org.example.orderservice.Repository;

import org.example.orderservice.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Object getProductById(Integer id);
}

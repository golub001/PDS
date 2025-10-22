package org.example.orderservice.Repository;

import org.example.orderservice.Entity.Product;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Integer getProductById(Integer id);

    @Query("select id from product where name=:name")
    Optional<Integer> getProductByName(@Param("name") String name);
}

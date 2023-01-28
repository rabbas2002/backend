package com.example.backend.repositories;

import com.example.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.supplier.id=?1 AND p.stock>0 AND p.name LIKE %?2% ")
     Page<Product> getBySupplierId(Long supplierId,String productName, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.supplier.id=?1 AND p.stock>0 AND p.name LIKE %?2% AND p.exp>CURRENT_DATE ")
    Page<Product>getUnexpiredProducts(Long supplierId,String productName,PageRequest of);
}


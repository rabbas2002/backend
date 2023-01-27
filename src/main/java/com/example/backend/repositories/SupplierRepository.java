package com.example.backend.repositories;

import com.example.backend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {
    @Query("SELECT s.id FROM Supplier s WHERE s.supplier=?1")
    Long getSupplierProducts(String supplierName );

}

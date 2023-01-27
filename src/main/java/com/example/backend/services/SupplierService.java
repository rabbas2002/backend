package com.example.backend.services;

import com.example.backend.model.Product;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class SupplierService {

    private Logger logger = LoggerFactory.getLogger(SupplierService.class);
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllSupplierProducts(String supplier, Long id,String productName,boolean isUnexpired ,int pageNo, int pageSize){
        //if supplier name is given
        if(supplier!=null){
            Long supplierId = supplierRepository.getSupplierProducts(supplier);
            logger.info("Getting suppliers using supplier name.....");
            if(isUnexpired){
                return productRepository.getUnexpiredProducts(supplierId,productName,PageRequest.of(pageNo,pageSize));
            }
            return productRepository.getBySupplierId(supplierId, productName,PageRequest.of(pageNo,pageSize));
        }
        else{
            logger.info("Getting suppliers using supplier Id......");
            if(isUnexpired){
                return productRepository.getUnexpiredProducts(id,productName,PageRequest.of(pageNo, pageSize));
            }
            return productRepository.getBySupplierId(id,productName,PageRequest.of(pageNo,pageSize));
        }

    }

}

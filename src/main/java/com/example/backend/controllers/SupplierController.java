package com.example.backend.controllers;

import com.example.backend.model.Product;
import com.example.backend.services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    private Logger logger = LoggerFactory.getLogger(SupplierController.class);
    @RequestMapping("/suppliers/{supplier}")
    public Page<Product> getSupplierProductsBySupplierName(
            @PathVariable String supplier,
            @RequestParam(defaultValue = "")String productName,
            @RequestParam(defaultValue = "false")boolean isUnexpired,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5")int pageSize
    ){
        try {
            Long id = Long.parseLong(supplier);
            logger.info("Fetching Products using Supplier Id.....");
            return supplierService.getAllSupplierProducts(null,id,productName,isUnexpired,pageNo,pageSize);
        }
        catch (NumberFormatException e){
            logger.info("Fetching Products using SupplierName.....");
            return supplierService.getAllSupplierProducts(supplier,null,productName,isUnexpired,pageNo,pageSize);
        }
    }
}

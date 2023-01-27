package com.example.backend.services;

import com.example.backend.dataloader.DataLoader;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.repositories.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository productRepository;

    private Logger logger = LoggerFactory.getLogger(ExcelService.class);
    public void save(MultipartFile file) {
        try {
            List[]arr = DataLoader.excelToProducts(file.getInputStream());
            productRepository.saveAll(arr[0]);
            supplierRepository.saveAll(arr[1]);
            logger.info("Persisted Data Successfully to the MySQL Database!");

        } catch (IOException e) {
            String message = "fail to store excel data: " + e.getMessage();
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
}

package dev.kishore.ecom.service;

import dev.kishore.ecom.exception.ProductNotFoundException;
import dev.kishore.ecom.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product getSingleProduct(Long productId) throws ProductNotFoundException;
    Page<Product> getAllProducts(int pageSize, int pageNumber, String fieldName);
    Product createProduct(Product product) throws ProductNotFoundException;
    Product updateProduct(Product product) throws ProductNotFoundException;
}

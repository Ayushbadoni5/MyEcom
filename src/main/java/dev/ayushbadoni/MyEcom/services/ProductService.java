package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.ProductDto;
import dev.ayushbadoni.MyEcom.entities.Product;

import java.util.List;

public interface ProductService {

    public Product addProduct(ProductDto product);

    public List<Product> getAllProducts();
}

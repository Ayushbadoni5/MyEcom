package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.ProductDto;
import dev.ayushbadoni.MyEcom.entities.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public Product addProduct(ProductDto product);

    public List<ProductDto> getAllProducts(UUID categoryId, UUID categoryTypeId);

    ProductDto getProductBySlug(String slug);

    ProductDto getProductById(UUID id);

    Product updateProduct(ProductDto productDto);

    Product fetchProductById(UUID uuid) throws Exception;

    void deleteProductById(UUID productId);
}

package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.ProductDto;
import dev.ayushbadoni.MyEcom.entities.Product;
import dev.ayushbadoni.MyEcom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;


    public Product addProduct(ProductDto product){

        return null;
    }
    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        // To-Do mapping into productDto
        return products;
    }
    private Product createProduct(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setNewArrival(product.isNewArrival());
        product.setPrice(productDto.getPrice());
        return product;
    }
}

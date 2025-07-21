package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.ProductDto;
import dev.ayushbadoni.MyEcom.entities.Product;
import dev.ayushbadoni.MyEcom.exceptions.ResourceNotFoundEx;
import dev.ayushbadoni.MyEcom.mapper.ProductMapper;
import dev.ayushbadoni.MyEcom.repositories.ProductRepository;
import dev.ayushbadoni.MyEcom.specifications.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    public Product addProduct(ProductDto productDto){
        Product product = productMapper.mapToProductEntity(productDto);
        return productRepository.save(product);
    }

    public List<ProductDto> getAllProducts(UUID categoryId, UUID TypeId){
        Specification<Product> productSpecification = Specification.where(null);

        if(null!= categoryId ){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if(null!= TypeId ){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(TypeId));
        }
        List<Product> products = productRepository.findAll(productSpecification);

        return productMapper.getProductDtos(products);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug);
        if (null == product){
            throw new ResourceNotFoundEx("Product Not Found!");
        }
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        return productDto;
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        ProductDto productDto = productMapper.mapProductToDto(product);
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setCategoryTypeId(product.getCategoryType().getId());
        productDto.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        return productDto;
    }

    @Override
    public Product updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        Product updatedProduct = productMapper.mapToProductEntity(productDto);

        return productRepository.save(updatedProduct);
    }

    public Product fetchProductById(UUID id) throws Exception {
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }

    @Override
    public void deleteProductById(UUID productId) {
        productRepository.deleteById(productId);
    }

}

package dev.ayushbadoni.MyEcom.mapper;


import dev.ayushbadoni.MyEcom.dto.ProductDto;
import dev.ayushbadoni.MyEcom.dto.ProductVariantDto;
import dev.ayushbadoni.MyEcom.entities.*;
import dev.ayushbadoni.MyEcom.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMapper {


    @Autowired
    private CategoryService categoryService;

    public Product mapToProductEntity(ProductDto productDto){
        Product product = new Product();
        if (null != productDto.getId()){
            product.setId(productDto.getId());
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBrand(productDto.getBrand());
        product.setNewArrival(productDto.isNewArrival());
        product.setRating(productDto.getRating());
        product.setSlug(productDto.getSlug());
        product.setPrice(productDto.getPrice());

        Category category = categoryService.getCategory(productDto.getCategoryId());
        if (null != category){
            product.setCategory(category);
            UUID categoryTypeId = productDto.getCategoryTypeId();
            CategoryType categoryType = category.getCategoryTypes().stream()
                    .filter(categoryType1 -> categoryType1.getId()
                            .equals(categoryTypeId)).findFirst().orElse(null);
            product.setCategoryType(categoryType);
        }


        if (null != productDto.getVariants()){
            product.setProductVariants(mapToProductVariant(productDto.getVariants(),product));
        }

        return product;
    }


    private List<ProductVariant> mapToProductVariant(List<ProductVariantDto> productVariantDtos, Product product){
        return productVariantDtos.stream().map(productVariantDto -> {
            ProductVariant productVariant = new ProductVariant();
            if (null != productVariantDto.getId()){
                productVariant.setId(productVariantDto.getId());
            }
            productVariant.setColor(productVariantDto.getColor());
            productVariant.setSize(productVariantDto.getSize());
            productVariant.setProduct(product);
            return productVariant;
        }).collect(Collectors.toList());
    }

    public List<ProductDto> getProductDtos(List<Product> products) {
        return products.stream().map(this::mapProductToDto).toList();
    }

    public ProductDto mapProductToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .brand(product.getBrand())
                .rating(product.getRating())
                .description(product.getDescription())
                .name(product.getName())
                .isNewArrival(product.isNewArrival())
                .slug(product.getSlug())
                .categoryId(product.getCategory() != null ? product.getCategory().getId(): null )
                .categoryName(product.getName() != null ? product.getCategory().getName(): null)
                .categoryTypeId(product.getCategoryType() != null ? product.getCategoryType().getId(): null)
                .categoryTypeName(product.getCategoryType() != null? product.getCategoryType().getName():null)
                .variants(mapProductVariantListToDto(product.getProductVariants()))
                .build();
    }

    public List<ProductVariantDto> mapProductVariantListToDto(List<ProductVariant> productVariants) {
        return productVariants.stream().map(this::mapProductVariantDto).toList();
    }

    private ProductVariantDto mapProductVariantDto(ProductVariant productVariant) {
        return ProductVariantDto.builder()
                .color(productVariant.getColor())
                .id(productVariant.getId())
                .size(productVariant.getSize())
                .build();
    }
}


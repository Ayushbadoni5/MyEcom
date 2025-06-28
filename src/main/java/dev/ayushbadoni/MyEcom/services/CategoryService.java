package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.CategoryDto;
import dev.ayushbadoni.MyEcom.dto.CategoryTypeDto;
import dev.ayushbadoni.MyEcom.entities.Category;
import dev.ayushbadoni.MyEcom.entities.CategoryType;
import dev.ayushbadoni.MyEcom.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public Category getCategory(UUID categoryID){
        Optional<Category> category = categoryRepository.findById(categoryID);
        return category.orElse(null);
    }

    public Category createCategory(CategoryDto categoryDto){
        Category category = mapToEntity(categoryDto);
        return categoryRepository.save(category);
    }

    private Category mapToEntity(CategoryDto categoryDto){
        Category category = Category.builder()
                .code(categoryDto.getCode())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .categoryTypes(mapToCategoryTypes(categoryDto.getCategoryType()))
                .build();
        return category;
    }

    private List<CategoryType> mapToCategoryTypes(List<CategoryTypeDto> categoryTypeList){
        return categoryTypeList.stream().map(categoryTypeDto -> {
            CategoryType categoryType = new CategoryType();
            categoryType.setCode(categoryTypeDto.getCode());
            categoryType.setName(categoryTypeDto.getName());
            categoryType.setDescription(categoryTypeDto.getDescription());
            return categoryType;
        }).collect((Collectors.toList()));
    }
}

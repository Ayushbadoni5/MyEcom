package dev.ayushbadoni.MyEcom.controllers;


import dev.ayushbadoni.MyEcom.dto.CategoryDto;
import dev.ayushbadoni.MyEcom.entities.Category;
import dev.ayushbadoni.MyEcom.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(value = "id",required = true) UUID categoryID){
        Category category = categoryService.getCategory(categoryID);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto){
        Category category = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categoryList = categoryService.getAllCategory();
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryDto categoryDto,
                                                   @PathVariable(value = "id",required = true) UUID categoryID){
        Category updatedCategory = categoryService.updateCategory(categoryDto,categoryID);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id",required = true) UUID categoryID){
        categoryService.deleteCategory(categoryID);
        return ResponseEntity.ok().build();
    }
}

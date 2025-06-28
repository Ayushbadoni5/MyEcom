package dev.ayushbadoni.MyEcom;

import dev.ayushbadoni.MyEcom.dto.ProductDto;

public class TestLombok {
    public static void main(String[] args) {
        ProductDto dto = ProductDto.builder()
                .name("Test Product")
                .description("Test Desc")
                .build();

        System.out.println(dto.getName());
    }
}

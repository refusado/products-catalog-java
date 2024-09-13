package com.refu.productscatalog;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 40)
    private String name;

    private String description = "";

    @NotNull
    @Positive
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantity = 0;

    private String createdAt;
    private String updatedAt;

    // validation for update requests (is optional, but if provided, must be valid)
    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.isBlank() &&
               price != null && price.compareTo(BigDecimal.ZERO) > 0 &&
               quantity != null && quantity >= 0;
    }
}
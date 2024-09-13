package com.refu.productscatalog.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
public class ProductUpdateDTO {
    @Size(min = 3, max = 40)
    private String name;

    private String description;

    @Positive
    private BigDecimal price;

    @PositiveOrZero
    private Integer quantity;

    // when updating, at least one field must be provided
    @JsonIgnore
    public boolean hasAtLeastOneField() {
        return name != null || description != null || price != null || quantity != null;
    }
}
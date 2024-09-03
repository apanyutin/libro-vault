package com.example.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemRequestDto {
    private Long shoppingCartId;
    @NotNull
    private Long bookId;
    @Min(1)
    private int quantity;
}

package com.example.dto.cart;

import com.example.model.Book;
import com.example.model.ShoppingCart;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartItemRequestDto {
    @NotNull
    private ShoppingCart shoppingCart;
    @NotNull
    private Book book;
    @Min(1)
    private int quantity;
}

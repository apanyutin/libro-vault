package com.example.dto.cart;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShoppingCartRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Set<Long> cartItemIds;
}

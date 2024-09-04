package com.example.dto.order;

import com.example.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequestDto {
    @NotNull
    private Order.Status status;
}

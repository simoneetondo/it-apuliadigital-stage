package it.exprivia.models.dtos.book;

import jakarta.validation.constraints.Min;

public record BookStockUpdateRequest(
        @Min(value=0, message = "Quantity can't be negative")
        Integer quantity
) {

}

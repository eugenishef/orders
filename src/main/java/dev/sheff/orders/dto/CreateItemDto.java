package dev.sheff.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateItemDto {
  @NotBlank
  private String barcode;

  @NotBlank
  private String name;

  private String type;
  private String age;

  @Positive(message = "Цена должна быть положительной")
  private Integer pricePerKg;

  @Positive(message = "Товаров должно быть > 0")
  private Integer quantity;
}

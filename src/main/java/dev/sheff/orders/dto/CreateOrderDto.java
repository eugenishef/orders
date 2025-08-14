package dev.sheff.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateOrderDto {
  private String comment;

  @NotNull(message = "Дата доставки обязательна")
  private LocalDateTime deliveryDate;

  @NotBlank(message = "Адрес доставки обязателен")
  private String deliveryAddress;

  @Valid
  private List<CreateItemDto> items;
}

package dev.sheff.orders.dto;

import dev.sheff.orders.model.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
  private Long id;
  private String number;
  private String deliveryAddress;
  private BigDecimal totalPrice;
  private Long userId;
  private String comment;
  private BigDecimal weight;
  private LocalDateTime createdAt;
  private LocalDateTime deliveryDate;
  private Status status;
  private List<ItemResponseDto> items;
}

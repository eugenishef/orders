package dev.sheff.orders.dto;

import dev.sheff.orders.model.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateOrderDto {
  private String comment;
  private LocalDateTime deliveryDate;
  private String deliveryAddress;
  private Status status;
}

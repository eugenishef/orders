package dev.sheff.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemResponseDto {
  private Long id;
  private String barcode;
  private String name;
  private String type;
  private String origin;
  private String age;
  private Integer pricePerKg;
  private Integer quantity;
  private BigDecimal weight;
  private boolean available;
}


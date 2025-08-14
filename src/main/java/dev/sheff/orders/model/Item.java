package dev.sheff.orders.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "items")
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

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

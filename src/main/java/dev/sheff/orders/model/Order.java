package dev.sheff.orders.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Заказы")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(unique = true, nullable = false)
  private String number;

  @Column(nullable = false)
  private String deliveryAddress;

  @Column(nullable = false)
  private BigDecimal totalPrice;

  @Column(nullable = false)
  private Long userId;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "order_id")
  private List<Item> items;

  private String comment;
  private BigDecimal weight;
  private LocalDateTime createdAt;
  private LocalDateTime deliveryDate;

  /**
   * TODO: Реализовать более сложную логику
   * - Как-то разделить операции вместо CascadeType.ALL
   * - orderNumber должен подтягиваться от пользователя сделавшего заказ
   */
}

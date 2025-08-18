package dev.sheff.orders.service;

import dev.sheff.orders.dto.CreateItemDto;
import dev.sheff.orders.dto.CreateOrderDto;
import dev.sheff.orders.dto.OrderResponseDto;
import dev.sheff.orders.dto.UpdateOrderDto;
import dev.sheff.orders.mapper.OrderMapper;
import dev.sheff.orders.model.Item;
import dev.sheff.orders.model.Order;
import dev.sheff.orders.model.Status;
import dev.sheff.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public List<Order> findAll() {
    List<Order> orders = orderRepository.findAll();

    return orders;
  }

  public Order findOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Заказ с id:" + id + "не найден"));
  }

  public Order findOrderByNumber(String number) {
    return orderRepository.findByNumber(number);
  }


  /**
   * TODO: Поправить запись в таблицу orders
   * - origin и weight не записываются
   */
  public Order createOrder(CreateOrderDto orderDto, Long userId) {
    List<Item> itemsList = orderDto.getItems().stream()
        .map(i -> Item.builder()
            .barcode(i.getBarcode())
            .name(i.getName())
            .type(i.getType())
            .age(i.getAge())
            .pricePerKg(i.getPricePerKg())
            .quantity(i.getQuantity())
            .available(true)
            .build()
        ).toList();

    Order order = Order.builder()
        .number(UUID.randomUUID().toString())
        .status(Status.NEW)
        .createdAt(LocalDateTime.now())
        .deliveryAddress(orderDto.getDeliveryAddress())
        .deliveryDate(orderDto.getDeliveryDate())
        .comment(orderDto.getComment())
        .userId(userId)
        .items(itemsList)
        .totalPrice(calculateTotalPrice(itemsList))
        .weight(calculateWeight(itemsList))
        .build();

    return orderRepository.save(order);
  }


  public Order updateOrderById(Long id, UpdateOrderDto request) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Заказ №" + id + " не найден"));

    Optional.ofNullable(request.getComment()).ifPresent(order::setComment);
    Optional.ofNullable(request.getDeliveryDate()).ifPresent(order::setDeliveryDate);
    Optional.ofNullable(request.getDeliveryAddress()).ifPresent(order::setDeliveryAddress);
    Optional.ofNullable(request.getStatus()).ifPresent(order::setStatus);

    return orderRepository.save(order);
  }

  public void deleteOrderById(Long id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Заказ №" + id + " не найден"));

    orderRepository.delete(order);
  }

  private BigDecimal calculateTotalPrice(List<Item> items) {
    return items.stream()
        .map(i -> BigDecimal.valueOf(i.getPricePerKg() * i.getQuantity()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calculateWeight(List<Item> items) {
    return items.stream()
        .map(i -> {
          if (i.getWeight() != null && i.getQuantity() != null) {
            return i.getWeight().multiply(BigDecimal.valueOf(i.getQuantity()));
          } else {
            return BigDecimal.ZERO;
          }
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}

package dev.sheff.orders.service;

import dev.sheff.orders.dto.CreateOrderDto;
import dev.sheff.orders.dto.OrderResponseDto;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public List<OrderResponseDto> findAll() {
    List<Order> orders = orderRepository.findAll();

    return orderMapper.toDtoList(orders);
  }

  public OrderResponseDto findOrderById(Long id) {
    Order order = orderRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Заказ с id:" + id + "не найден"));

    return orderMapper.toDto(order);
  }

  public OrderResponseDto findOrderByNumber(String number) {
    Order order = orderRepository
        .findByNumber(number);

    return orderMapper.toDto(order);
  }



  /**
   * TODO: Поправить запись в таблицу orders
   * - origin и weight не записываются
   */
  public Order createOrder(CreateOrderDto orderDto, Long userId) {
    Order order = new Order();

    order.setNumber(UUID.randomUUID().toString());
    order.setStatus(Status.NEW);
    order.setCreatedAt(LocalDateTime.now());

    order.setDeliveryAddress(orderDto.getDeliveryAddress());
    order.setDeliveryDate(orderDto.getDeliveryDate());
    order.setComment(order.getComment());
    order.setUserId(userId);

    List<Item> itemsList = orderDto.getItems().stream().map(i -> {
      Item item = new Item();

      item.setBarcode(i.getBarcode());
      item.setName(i.getName());
      item.setType(i.getType());
      item.setAge(i.getAge());
      item.setPricePerKg(i.getPricePerKg());
      item.setQuantity(i.getQuantity());
      item.setAvailable(true);

      return item;
    }).toList();

    order.setItems(itemsList);
    order.setTotalPrice(calculateTotalPrice(itemsList));
    order.setWeight(calculateWeight(itemsList));

    return orderRepository.save(order);
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

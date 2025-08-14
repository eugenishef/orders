package dev.sheff.orders.mapper;

import dev.sheff.orders.dto.ItemResponseDto;
import dev.sheff.orders.dto.OrderResponseDto;
import dev.sheff.orders.model.Item;
import dev.sheff.orders.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
  public OrderResponseDto toDto(Order order) {
    if (order == null) return null;

    OrderResponseDto dto = new OrderResponseDto();

    dto.setId(order.getId());
    dto.setNumber(order.getNumber());
    dto.setDeliveryAddress(order.getDeliveryAddress());
    dto.setTotalPrice(order.getTotalPrice());
    dto.setUserId(order.getUserId());
    dto.setComment(order.getComment());
    dto.setWeight(order.getWeight());
    dto.setCreatedAt(order.getCreatedAt());
    dto.setDeliveryDate(order.getDeliveryDate());
    dto.setStatus(order.getStatus());

    List<ItemResponseDto> items = order.getItems()
        .stream()
        .map(this::mapItem)
        .toList();

    dto.setItems(items);

    return dto;
  }

  private ItemResponseDto mapItem(Item item) {
    ItemResponseDto dto = new ItemResponseDto();

    dto.setId(item.getId());
    dto.setBarcode(item.getBarcode());
    dto.setName(item.getName());
    dto.setType(item.getType());
    dto.setOrigin(item.getOrigin());
    dto.setAge(item.getAge());
    dto.setPricePerKg(item.getPricePerKg());
    dto.setQuantity(item.getQuantity());
    dto.setWeight(item.getWeight());
    dto.setAvailable(item.isAvailable());

    return dto;
  }

  public List<OrderResponseDto> toDtoList(List<Order> orders) {
    return orders.stream().map(this::toDto).collect(Collectors.toList());
  }
}

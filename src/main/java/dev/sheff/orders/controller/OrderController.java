package dev.sheff.orders.controller;

import dev.sheff.orders.dto.CreateOrderDto;
import dev.sheff.orders.dto.OrderResponseDto;
import dev.sheff.orders.model.Order;
import dev.sheff.orders.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @GetMapping("/orders/all")
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    return ResponseEntity.ok(orderService.findAll());
  }

  @GetMapping("/order/{id}")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") Long id) {
    OrderResponseDto dto = orderService.findOrderById(id);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/order")
  public ResponseEntity<?> getOrder(
      @RequestParam(value = "id", required = false) String idStr,
      @RequestParam(value = "number", required = false) String number) {

    boolean hasId = idStr != null && !idStr.isBlank();
    boolean hasNumber = number != null && !number.isBlank();

    if (!hasId && !hasNumber) {
      return ResponseEntity.badRequest()
          .body(Map.of("status", 400, "message", "Укажите id или number"));
    }

    try {
      if (hasId) {
        Long id = Long.parseLong(idStr.trim());
        OrderResponseDto dto = orderService.findOrderById(id);
        return ResponseEntity.ok(dto);
      } else {
        OrderResponseDto dto = orderService.findOrderByNumber(number.trim());
        return ResponseEntity.ok(dto);
      }
    } catch (NumberFormatException ex) {
      return ResponseEntity.badRequest()
          .body(Map.of("status", 400, "message", "id должен быть числом"));
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Map.of("status", 404, "message", ex.getMessage()));
    } catch (Exception ex) {
      log.error("Error in getOrder", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("status", 500, "message", "Внутренняя ошибка"));
    }
  }


  @PostMapping("/orders")
  public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderDto request) {
    Long mockUserId = 1L;

    Order order = orderService.createOrder(request, mockUserId);
    return ResponseEntity.ok(order);
  }
}

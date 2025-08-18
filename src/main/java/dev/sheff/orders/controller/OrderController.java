package dev.sheff.orders.controller;

import dev.sheff.orders.constraints.ApiRoutes;
import dev.sheff.orders.dto.CreateOrderDto;
import dev.sheff.orders.dto.OrderResponseDto;
import dev.sheff.orders.dto.UpdateOrderDto;
import dev.sheff.orders.mapper.OrderMapper;
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


/**
 * Контроллер для управления заказами.
 * <p>
 * Предоставляет REST API для:
 * <ul>
 *     <li>{@link #createOrder(CreateOrderDto) Создания заказа}</li>
 *     <li>{@link #getAllOrders() Получения списка всех заказов}</li>
 *     <li>{@link #getOrderById(Long) Получения заказа по ID}</li>
 *     <li>{@link #getOrder(String, String) Поиска заказов по номеру и статусу}</li>
 * </ul>
 *
 * Базовый URL: {@link dev.sheff.orders.constraints.ApiRoutes#ORDERS}
 *
 * @author Evgenii Shevchenko
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(ApiRoutes.ORDERS)
@RequiredArgsConstructor
public class OrderController {

  private final OrderMapper orderMapper;

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderDto request) {
    Long mockUserId = 1L;

    Order order = orderService.createOrder(request, mockUserId);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    List<Order> orderList = orderService.findAll();
    return ResponseEntity.ok(orderMapper.toDtoList(orderList));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") Long id) {
    Order order = orderService.findOrderById(id);

    return ResponseEntity.ok(orderMapper.toDto(order));
  }

  @GetMapping("/search")
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
        Order order = orderService.findOrderById(id);
        return ResponseEntity.ok(orderMapper.toDto(order));
      } else {
        Order order = orderService.findOrderByNumber(number.trim());
        return ResponseEntity.ok(orderMapper.toDto(order));
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

  @PutMapping("/{id}")
  public ResponseEntity<OrderResponseDto> chageOrderById(@PathVariable("id") Long id, @Valid @RequestBody UpdateOrderDto request) {
    Order order = orderService.updateOrderById(id, request);

    return ResponseEntity.ok(orderMapper.toDto(order));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderById(@PathVariable("id") Long id) {
    orderService.deleteOrderById(id);

    return ResponseEntity.ok("Заказ был успешно удален");
  }
}

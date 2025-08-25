package com.company.tenant1.app.controller;

import com.company.core.dto.ApiResponse;
import com.company.core.model.Order;
import com.company.core.model.User;
import com.company.core.processor.OrderProcessor;
import com.company.tenant1.app.dto.CreateOrderRequest;
import com.company.tenant1.app.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderProcessor orderProcessor;

    // 简单内存存储，演示用
    private final Map<String, Order> orderStorage = new HashMap<>();
    private final Map<String, User> userStorage = new HashMap<>();

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            // 验证请求参数
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("用户ID不能为空", "INVALID_USER_ID"));
            }
            
            if (request.getAmount() == null || request.getAmount().signum() <= 0) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("订单金额必须大于0", "INVALID_AMOUNT"));
            }

            // 创建用户对象
            User user = new User(request.getUserId(), request.getUserName(), 
                               request.getUserEmail(), request.getUserPhone());
            userStorage.put(user.getId(), user);

            // 创建订单
            Order order = new Order(java.util.UUID.randomUUID().toString(), 
                                  request.getUserId(), request.getAmount());
            order.setUser(user);
            
            // 处理订单
            orderProcessor.process(order);
            
            // 存储订单
            orderStorage.put(order.getId(), order);
            
            // 转换为DTO并返回
            OrderDto dto = convertToDto(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("订单创建成功", dto));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("创建订单失败: " + e.getMessage(), "CREATE_FAILED"));
        }
    }

    /**
     * 根据ID获取订单详情
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable String orderId) {
        try {
            Order order = orderStorage.get(orderId);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("订单不存在", "ORDER_NOT_FOUND"));
            }
            
            OrderDto dto = convertToDto(order);
            return ResponseEntity.ok(ApiResponse.success(dto));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("获取订单失败: " + e.getMessage(), "GET_FAILED"));
        }
    }

    /**
     * 获取订单列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Order> allOrders = new ArrayList<>(orderStorage.values());
            
            // 按创建时间倒序排列
            allOrders.sort((o1, o2) -> {
                if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) return 0;
                if (o1.getCreatedAt() == null) return 1;
                if (o2.getCreatedAt() == null) return -1;
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            });
            
            // 简单分页
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, allOrders.size());
            
            List<Order> pageOrders = new ArrayList<>();
            if (startIndex < allOrders.size()) {
                pageOrders = allOrders.subList(startIndex, endIndex);
            }
            
            // 转换为DTO
            List<OrderDto> dtos = pageOrders.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
                
            return ResponseEntity.ok(ApiResponse.success(dtos));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("获取订单列表失败: " + e.getMessage(), "LIST_FAILED"));
        }
    }

    /**
     * 获取租户1特有的订单统计信息
     */
    @GetMapping("/tenant1-statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTenant1Statistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 基础统计
            statistics.put("totalOrders", orderStorage.size());
            statistics.put("tenantId", "tenant1");
            statistics.put("tenantName", "高级租户");
            
            // 按状态统计
            Map<String, Long> statusCount = orderStorage.values().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    order -> order.getStatus() != null ? order.getStatus() : "UNKNOWN",
                    java.util.stream.Collectors.counting()
                ));
            statistics.put("statusStatistics", statusCount);
            
            // 特有功能统计
            statistics.put("smsNotificationsEnabled", true);
            statistics.put("auditRequiredOrders", orderStorage.values().stream()
                .mapToLong(order -> order.getAmount().compareTo(new java.math.BigDecimal("10000")) > 0 ? 1 : 0)
                .sum());
            
            statistics.put("generatedAt", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponse.success("租户1统计信息获取成功", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("获取统计信息失败: " + e.getMessage(), "STATISTICS_FAILED"));
        }
    }

    /**
     * 重新处理订单（租户1特有功能）
     */
    @PostMapping("/{orderId}/reprocess")
    public ResponseEntity<ApiResponse<String>> reprocessOrder(@PathVariable String orderId) {
        try {
            Order order = orderStorage.get(orderId);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("订单不存在", "ORDER_NOT_FOUND"));
            }
            
            // 重新处理订单
            orderProcessor.process(order);
            
            return ResponseEntity.ok(ApiResponse.success("订单重新处理成功", "订单已重新通过租户1的处理流程"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("重新处理订单失败: " + e.getMessage(), "REPROCESS_FAILED"));
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("tenantId", "tenant1");
            health.put("status", "健康");
            health.put("totalOrders", orderStorage.size());
            health.put("checkTime", java.time.LocalDateTime.now());
            health.put("features", java.util.Arrays.asList("SMS通知", "高额订单审核", "库存更新"));
            
            return ResponseEntity.ok(ApiResponse.success("健康检查完成", health));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("健康检查失败: " + e.getMessage(), "HEALTH_CHECK_FAILED"));
        }
    }

    /**
     * 转换Order为OrderDto
     */
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setAmount(order.getAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        
        if (order.getUser() != null) {
            dto.setUserName(order.getUser().getName());
            dto.setUserPhone(order.getUser().getPhone());
        }
        
        return dto;
    }
}
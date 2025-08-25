package com.company.tenant2.app.controller;

import com.company.core.dto.ApiResponse;
import com.company.core.model.Order;
import com.company.core.model.User;
import com.company.core.processor.OrderProcessor;
import com.company.tenant2.app.dto.CreateOrderRequest;
import com.company.tenant2.app.dto.OrderDto;
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

    private final Map<String, Order> orderStorage = new HashMap<>();
    private final Map<String, User> userStorage = new HashMap<>();
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("用户ID不能为空", "INVALID_USER_ID"));
            }
            
            if (request.getAmount() == null || request.getAmount().signum() <= 0) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("订单金额必须大于0", "INVALID_AMOUNT"));
            }

            if (request.getEnterpriseId() == null || request.getEnterpriseId().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("企业ID不能为空", "INVALID_ENTERPRISE_ID"));
            }

            User user = new User(request.getUserId(), request.getUserName(), 
                               request.getUserEmail(), request.getUserPhone());
            userStorage.put(user.getId(), user);

            Order order = new Order(java.util.UUID.randomUUID().toString(), 
                                  request.getUserId(), request.getAmount());
            order.setUser(user);
            
            orderProcessor.process(order);
            
            orderStorage.put(order.getId(), order);
            
            OrderDto dto = convertToDto(order, request.getOrderType(), request.getEnterpriseId());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("企业订单创建成功", dto));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("创建企业订单失败: " + e.getMessage(), "CREATE_FAILED"));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable String orderId) {
        try {
            Order order = orderStorage.get(orderId);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("订单不存在", "ORDER_NOT_FOUND"));
            }
            
            OrderDto dto = convertToDto(order, "ENTERPRISE", "ENT-001");
            return ResponseEntity.ok(ApiResponse.success(dto));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("获取订单失败: " + e.getMessage(), "GET_FAILED"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String enterpriseId) {
        try {
            List<Order> allOrders = new ArrayList<>(orderStorage.values());
            
            allOrders.sort((o1, o2) -> {
                if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) return 0;
                if (o1.getCreatedAt() == null) return 1;
                if (o2.getCreatedAt() == null) return -1;
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            });
            
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, allOrders.size());
            
            List<Order> pageOrders = new ArrayList<>();
            if (startIndex < allOrders.size()) {
                pageOrders = allOrders.subList(startIndex, endIndex);
            }
            
            List<OrderDto> dtos = pageOrders.stream()
                .map(order -> convertToDto(order, "ENTERPRISE", enterpriseId != null ? enterpriseId : "ENT-001"))
                .collect(java.util.stream.Collectors.toList());
                
            return ResponseEntity.ok(ApiResponse.success(dtos));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("获取订单列表失败: " + e.getMessage(), "LIST_FAILED"));
        }
    }

    @GetMapping("/tenant2-statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTenant2Statistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            statistics.put("totalOrders", orderStorage.size());
            statistics.put("tenantId", "tenant2");
            statistics.put("tenantName", "企业租户");
            
            Map<String, Long> statusCount = orderStorage.values().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    order -> order.getStatus() != null ? order.getStatus() : "UNKNOWN",
                    java.util.stream.Collectors.counting()
                ));
            statistics.put("statusStatistics", statusCount);
            
            statistics.put("approvalWorkflowEnabled", true);
            statistics.put("integrationEnabled", true);
            statistics.put("highValueOrders", orderStorage.values().stream()
                .mapToLong(order -> order.getAmount().compareTo(new java.math.BigDecimal("50000")) > 0 ? 1 : 0)
                .sum());
            
            statistics.put("workflowTriggered", orderStorage.size());
            statistics.put("generatedAt", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponse.success("租户2统计信息获取成功", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("获取统计信息失败: " + e.getMessage(), "STATISTICS_FAILED"));
        }
    }

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<ApiResponse<String>> approveOrder(@PathVariable String orderId) {
        try {
            Order order = orderStorage.get(orderId);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("订单不存在", "ORDER_NOT_FOUND"));
            }
            
            order.setStatus("APPROVED");
            
            return ResponseEntity.ok(ApiResponse.success("订单审批通过", "订单已通过企业审批流程"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("订单审批失败: " + e.getMessage(), "APPROVAL_FAILED"));
        }
    }

    @PostMapping("/{orderId}/workflow-restart")  
    public ResponseEntity<ApiResponse<String>> restartWorkflow(@PathVariable String orderId) {
        try {
            Order order = orderStorage.get(orderId);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("订单不存在", "ORDER_NOT_FOUND"));
            }
            
            orderProcessor.process(order);
            
            return ResponseEntity.ok(ApiResponse.success("工作流重启成功", "订单工作流已重新启动"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("工作流重启失败: " + e.getMessage(), "WORKFLOW_RESTART_FAILED"));
        }
    }

    @GetMapping("/integration-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getIntegrationStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("integrationEnabled", true);
            status.put("systemStatus", "在线");
            status.put("version", "Enterprise-v3.2.1");
            status.put("connectedServices", java.util.Arrays.asList("订单系统", "工作流系统", "审批系统"));
            status.put("lastHealthCheck", java.time.LocalDateTime.now());
            status.put("totalIntegrations", 3);
            
            return ResponseEntity.ok(ApiResponse.success("集成状态获取成功", status));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("获取集成状态失败: " + e.getMessage(), "INTEGRATION_STATUS_FAILED"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("tenantId", "tenant2");
            health.put("status", "健康");
            health.put("totalOrders", orderStorage.size());
            health.put("checkTime", java.time.LocalDateTime.now());
            health.put("features", java.util.Arrays.asList("企业工作流", "订单审批", "系统集成"));
            health.put("mode", "企业模式");
            
            return ResponseEntity.ok(ApiResponse.success("健康检查完成", health));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(ApiResponse.error("健康检查失败: " + e.getMessage(), "HEALTH_CHECK_FAILED"));
        }
    }

    private OrderDto convertToDto(Order order, String orderType, String enterpriseId) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setAmount(order.getAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setOrderType(orderType);
        dto.setEnterpriseId(enterpriseId);
        dto.setWorkflowStatus("ACTIVE");
        
        if (order.getUser() != null) {
            dto.setUserName(order.getUser().getName());
            dto.setUserPhone(order.getUser().getPhone());
        }
        
        return dto;
    }
}
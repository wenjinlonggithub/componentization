# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在该代码仓库中工作时提供指导。

## 🎯 精简组件化架构

**设计理念**：用最少的模块展示完整的组件化设计精髓

### 📦 **5个核心模块**
```
📂 core-system/              # 核心框架 + 通用业务抽象
📂 customization-tenant1/    # 继承扩展模式示例  
📂 customization-tenant2/    # 事件驱动模式示例
📂 tenant1-app/             # 租户1企业版应用
📂 tenant2-app/             # 租户2专业版应用
```

**核心优势**：
- ✅ 模块精简：仅5个模块，易于理解和维护
- ✅ 功能完整：涵盖所有组件化扩展模式
- ✅ 场景丰富：支持订单、医疗等多业务场景
- ✅ 对比清晰：两种扩展模式直接对比

## 🚀 快速启动

### 构建所有模块
```bash
mvn clean install
```

### 🏢 租户1企业版 - 继承扩展模式（端口 8081）
```bash
cd tenant1-app && mvn spring-boot:run
```

**特色功能**：
- 深度定制的企业级功能
- 高额订单审批流程
- 多级企业认证
- 完整审计轨迹

**API示例**：
```bash
# 传统订单处理
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": "user001", "userName": "张三", "amount": 15000}'

# 通用业务处理
curl -X POST http://localhost:8081/api/business/order \
  -H "Content-Type: application/json" \
  -d '{"amount": 15000, "itemCount": 5, "operatorId": "manager001"}'

# 系统信息
curl http://localhost:8081/api/business/info
```

### 🔬 租户2专业版 - 事件驱动模式（端口 8082）
```bash
cd tenant2-app && mvn spring-boot:run
```

**特色功能**：
- 智能风控检测
- 快速处理通道
- 增值服务套件
- 业务数据洞察

**API示例**：
```bash
# 智能订单处理
curl -X POST http://localhost:8082/api/business/order \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000, "itemCount": 2, "operatorId": "analyst001"}'

# 智能分析（专业版独有）
curl -X POST http://localhost:8082/api/business/analytics \
  -H "Content-Type: application/json" \
  -d '{"dataType": "sales", "period": "monthly"}'
```

## 架构设计精髓

这是一个**组件化架构学习项目**，专注展示两种核心扩展模式的设计思想与实践对比。

**设计理念**：通过简洁的订单处理场景，深刻理解组件化架构的扩展点设计与多租户定制化能力。

### 核心扩展模式对比

| 扩展模式 | 继承扩展 (Tenant1) | 事件驱动 (Tenant2) |
|---------|------------------|-------------------|
| **耦合度** | 强耦合 | 松耦合 |
| **适用场景** | 完全替换核心逻辑 | 功能增强与协同 |
| **扩展方式** | 覆盖钩子方法 | 监听事件响应 |
| **代码侵入** | 需要继承 | 独立监听器 |

### 多模块结构

- **core-system**: 包含基础功能和扩展点（`OrderProcessor` 接口）
- **customization-tenant1**: 租户1定制化，使用继承+事件混合方法
- **customization-tenant2**: 租户2定制化，使用纯事件驱动方法
- **tenant1-app**: 租户1的可部署应用（高级版功能）
- **tenant2-app**: 租户2的可部署应用（企业版功能）

### 设计模式详解

#### 1. 模板方法模式 + 钩子扩展
**核心类**：`DefaultOrderProcessor`
- **主流程固定**：validate → calculatePrice → save → notify
- **扩展钩子**：`beforeSave()` 和 `beforeNotify()` 空方法
- **扩展方式**：子类继承并覆盖钩子方法

#### 2. 事件驱动架构
**事件体系**：统一的 `OrderEvent` 基类
- `BeforeSaveEvent` / `AfterSaveEvent` / `BeforeNotifyEvent`
- **条件执行**：`skipDefaultAction()` 机制支持替换默认行为
- **松耦合**：监听器独立于核心流程

#### 3. 条件化装配
**配置驱动**：通过 `tenant.id` 属性激活特定定制化
```java
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
```

### 实际定制化对比

**租户1 - 继承扩展模式**
```java
// 直接覆盖钩子方法，简单直接
@Override
protected void beforeSave(Order order) {
    if (requiresAudit(order)) {
        order.setStatus("PENDING_APPROVAL");
    }
}
```

**租户2 - 事件驱动模式**
```java
// 监听事件，松耦合扩展
@EventListener
public void handleBeforeNotify(BeforeNotifyEvent event) {
    sendMultiChannelNotification(event.getOrder());
    event.skipDefaultAction();  // 替换默认行为
}
```

### 学习要点

| 关键概念 | 实现位置 | 学习价值 |
|---------|---------|---------|
| **扩展点设计** | DefaultOrderProcessor:78-79 | 钩子方法的最小化设计 |
| **事件机制** | OrderEvent 基类 | 统一事件接口减少重复 |
| **条件执行** | skipDefaultAction() | 事件如何影响主流程 |
| **模式对比** | 两个租户实现 | 不同扩展方式的适用场景 |

### 配置管理

每个租户应用使用 `tenant.id` 属性激活特定定制化：
- `tenant.id=tenant1` 激活 Tenant1Config 和相关bean
- `tenant.id=tenant2` 激活 Tenant2Config 和相关bean

核心系统使用 `@ConditionalOnMissingBean(OrderProcessor.class)` 在没有租户特定处理器时提供默认实现。

### 开发工作流程

添加新租户定制化时：
1. 按照现有模式创建新的定制化模块
2. 选择扩展方法：继承（直接钩子覆盖）或事件驱动（松耦合）
3. 添加带有唯一 `tenant.id` 值的条件化配置
4. 创建包含核心+定制化依赖的可部署应用模块
5. 在 application.yml 中配置适当的 tenant.id 和其他租户特定属性

### 重要实现细节

- 事件同步发布，支持条件跳过默认行为
- `@Order` 注解控制事件监听器执行顺序
- 租户模块仅依赖 core-system，避免跨租户依赖
- 每个可部署应用运行在不同端口（8081、8082）以支持独立部署

## 📋 模块管理规范

**⚠️ 严格模块控制：本项目采用精简的5模块架构，禁止随意新增或变更模块！**

### 核心模块（固定不变）

1. **core-system** - 核心系统和扩展点
2. **customization-tenant1** - 租户1定制化（继承扩展模式）
3. **customization-tenant2** - 租户2定制化（事件驱动模式）
4. **tenant1-app** - 租户1可部署应用
5. **tenant2-app** - 租户2可部署应用

### 新功能开发原则

#### ✅ 允许的操作
- 在现有5个模块内**扩展功能**
- 在 core-system 中**新增通用能力**
- 在 customization 模块中**增加定制化逻辑**
- 在 app 模块中**添加新的API接口**
- **升级依赖版本**和**优化现有代码**

#### ❌ 禁止的操作
- **新增任何模块**（无论是业务模块、工具模块或其他模块）
- **删除现有的5个核心模块**
- **重命名模块**或**改变模块结构**
- **在模块间新增依赖关系**（除已定义的依赖链）

### 扩展新业务场景的正确方式

#### 方案1：扩展 core-system
```java
// 在 core-system 中新增业务处理能力
public class UniversalProcessor {
    public ProcessResult processBusiness(BusinessContext context) {
        // 支持 order, medical, analytics, 新业务...
    }
}
```

#### 方案2：扩展定制化模块
```java
// 在 customization-tenant1 中处理新业务
@Override
protected void beforeProcess(BusinessContext context) {
    if ("newBusiness".equals(context.getScenario())) {
        handleNewBusiness(context);
    }
}
```

#### 方案3：扩展应用层API
```java
// 在 tenant-app 中新增业务接口
@PostMapping("/api/business/newservice")
public ProcessResult processNewService(@RequestBody Map<String, Object> data) {
    // 处理新业务
}
```

### 违规检查清单

在提交任何代码修改前，请确认：
- [ ] 没有新增任何模块目录
- [ ] pom.xml 中只包含5个核心模块
- [ ] 没有新增模块间的依赖关系
- [ ] 新功能已正确归类到现有模块中

### 架构决策记录

**设计原则**：用最少的模块展示最完整的设计思想
**模块数量**：精简至5个核心模块，拒绝膨胀
**扩展方式**：垂直扩展（模块内功能增强）而非水平扩展（新增模块）
**学习目标**：降低学习成本，突出核心架构模式
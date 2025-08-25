# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在该代码仓库中工作时提供指导。

## 构建和开发命令

### 构建所有模块
```bash
mvn clean install
```

### 运行特定租户应用（包含REST API接口）
```bash
# 运行租户1应用（高级版 - 端口 8081）
# REST API端点：http://localhost:8081/api/orders
cd tenant1-app && mvn spring-boot:run

# 运行租户2应用（企业版 - 端口 8082） 
# REST API端点：http://localhost:8082/api/orders
cd tenant2-app && mvn spring-boot:run

# 仅运行核心系统（端口 8080）
cd core-system && mvn spring-boot:run
```

### 构建单个模块
```bash
# 构建核心系统
cd core-system && mvn clean install

# 构建特定租户定制化模块
cd customization-tenant1 && mvn clean install
cd customization-tenant2 && mvn clean install
```

## 架构概览

这是一个**组件化架构**，演示了使用 Spring Boot 的多租户定制化模式。系统通过两种主要扩展方法支持标准化功能和租户特定定制。

**项目演示场景**：
- **订单处理系统**：通过简单明了的订单处理业务，清晰展示组件化架构的核心设计模式和租户定制化能力

### 多模块结构

- **core-system**: 包含基础功能和扩展点（`OrderProcessor` 接口）
- **customization-tenant1**: 租户1定制化，使用继承+事件混合方法
- **customization-tenant2**: 租户2定制化，使用纯事件驱动方法
- **tenant1-app**: 租户1的可部署应用（高级版功能）
- **tenant2-app**: 租户2的可部署应用（企业版功能）

### 核心扩展模式

#### 1. 钩子方法（模板方法模式）
`DefaultOrderProcessor` 类定义主要处理流程，包含可覆盖的钩子方法：
- `beforeSave(Order order)` - 保存前定制化钩子
- `beforeNotify(Order order)` - 通知前定制化钩子

租户定制化可以继承 `DefaultOrderProcessor` 并覆盖这些钩子。

#### 2. 事件驱动扩展
核心系统在关键处理点发布 Spring 事件：
- `BeforeSaveEvent` - 保存订单前发布
- `AfterSaveEvent` - 成功保存后发布
- `BeforeNotifyEvent` - 用户通知前发布

事件支持通过 `skipDefaultAction()` 机制进行条件执行。

#### 3. 条件化配置
租户模块通过 `@ConditionalOnProperty` 激活：
```java
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
```

### 租户定制化示例

**租户1（高级版）** - 使用继承+事件混合方式：
- `CustomOrderProcessor extends DefaultOrderProcessor` - 覆盖钩子实现高额订单审核和短信通知
- `OrderEventListener` - 通过事件监听器处理额外业务逻辑

**租户2（企业版）** - 使用纯事件驱动方式：
- `AdvancedOrderEventListener` - 完全通过事件处理实现复杂验证、ERP集成和多渠道通知

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
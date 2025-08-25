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

## 🔗 模块依赖关系

### 依赖层次结构

```
🏗️  应用层 (Application Layer)
┌─────────────────┬─────────────────┐
│   tenant1-app   │   tenant2-app   │
│   (企业版应用)   │   (专业版应用)   │
└─────────┬───────┴─────────┬───────┘
          │                 │
          ▼                 ▼
🔧  定制层 (Customization Layer)
    ┌─────────────────┬─────────────────┐
    │customization-   │customization-   │
    │tenant1          │tenant2          │
    │(继承扩展模式)    │(事件驱动模式)    │
    └─────────┬───────┴─────────┬───────┘
              │                 │
              ▼                 ▼
🎯  核心层 (Core Layer)
        ┌─────────────────────┐
        │    core-system      │
        │   (框架+扩展点)      │
        └─────────────────────┘
```

### 详细依赖关系

#### 1. **core-system** (基础层 - 0依赖)
```xml
<dependencies>
  <!-- 只依赖Spring Boot基础组件 -->
  <dependency>spring-boot-starter</dependency>
  <dependency>spring-boot-starter-web</dependency>
  <dependency>spring-boot-autoconfigure</dependency>
  <dependency>spring-context</dependency>
</dependencies>
```

**职责**：
- 提供核心业务抽象 (BusinessContext, UniversalProcessor)
- 定义扩展点和钩子方法
- 发布统一事件机制 (BusinessEvent)
- 提供默认实现 (DefaultOrderProcessor)

#### 2. **customization-tenant1** (定制层 - 依赖core)
```xml
<dependencies>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>core-system</artifactId>  <!-- 依赖核心系统 -->
  </dependency>
</dependencies>
```

**职责**：
- 继承 UniversalProcessor，覆盖钩子方法
- 实现企业级深度定制逻辑
- 提供条件化配置 (@ConditionalOnProperty)

#### 3. **customization-tenant2** (定制层 - 依赖core)
```xml
<dependencies>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>core-system</artifactId>  <!-- 依赖核心系统 -->
  </dependency>
</dependencies>
```

**职责**：
- 监听 BusinessEvent，实现松耦合扩展
- 提供专业版智能增值服务
- 事件驱动的功能增强

#### 4. **tenant1-app** (应用层 - 依赖core+tenant1定制)
```xml
<dependencies>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>core-system</artifactId>         <!-- 核心能力 -->
  </dependency>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>customization-tenant1</artifactId> <!-- 租户1定制 -->
  </dependency>
</dependencies>
```

**职责**：
- 提供完整的企业版应用
- 暴露REST API接口
- 集成核心系统+租户1定制化

#### 5. **tenant2-app** (应用层 - 依赖core+tenant2定制)
```xml
<dependencies>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>core-system</artifactId>         <!-- 核心能力 -->
  </dependency>
  <dependency>
    <groupId>com.company</groupId>
    <artifactId>customization-tenant2</artifactId> <!-- 租户2定制 -->
  </dependency>
</dependencies>
```

**职责**：
- 提供完整的专业版应用  
- 暴露REST API接口
- 集成核心系统+租户2定制化

### 依赖设计原则

#### ✅ 良好的依赖设计
1. **单向依赖**：依赖关系清晰，无循环依赖
2. **分层依赖**：应用层 → 定制层 → 核心层
3. **最小依赖**：每个模块只依赖必需的上游模块
4. **职责分离**：每层有明确的职责边界

#### ❌ 禁止的依赖关系
- core-system **不能**依赖任何定制化模块
- customization-tenant1 **不能**依赖 customization-tenant2
- customization-tenant2 **不能**依赖 customization-tenant1  
- tenant1-app **不能**依赖 tenant2相关模块
- tenant2-app **不能**依赖 tenant1相关模块

### 调用链路分析

#### 租户1调用链 (继承扩展模式)
```
🌐 HTTP Request
    ↓
📱 UniversalController (tenant1-app)
    ↓
🔧 CustomOrderProcessor (customization-tenant1) 
    ↓ [继承]
🎯 UniversalProcessor (core-system)
    ↓ [钩子调用]
🔧 CustomOrderProcessor.beforeProcess() (定制逻辑)
```

#### 租户2调用链 (事件驱动模式)
```
🌐 HTTP Request  
    ↓
📱 UniversalController (tenant2-app)
    ↓
🎯 UniversalProcessor (core-system)
    ↓ [发布事件]
📡 BusinessEvent
    ↓ [监听处理] 
🔧 AdvancedOrderEventListener (customization-tenant2)
```

### 依赖检查工具

#### Maven依赖树查看
```bash
# 查看tenant1-app完整依赖树
cd tenant1-app && mvn dependency:tree

# 查看tenant2-app完整依赖树  
cd tenant2-app && mvn dependency:tree
```

#### 依赖验证清单
在修改依赖前必须检查：
- [ ] 是否引入循环依赖
- [ ] 是否违反分层原则
- [ ] 是否增加不必要的传递依赖
- [ ] 是否破坏模块职责边界

## 🎯 丰富业务案例展示

### 业务场景全览

#### 租户1企业版 - 深度定制业务场景
```bash
# 订单处理 - 企业级审批流程
curl -X POST http://localhost:8081/api/business/order \
  -H "Content-Type: application/json" \
  -d '{"amount": 15000, "itemCount": 5, "customerType": "enterprise"}'

# 医疗管理 - 多级认证审批  
curl -X POST http://localhost:8081/api/business/medical \
  -H "Content-Type: application/json" \
  -d '{"patientId": "P001", "treatment": "surgery", "doctorId": "Dr001"}'

# 财务管理 - 企业级支出审计
curl -X POST http://localhost:8081/api/business/finance \
  -H "Content-Type: application/json" \
  -d '{"amount": 50000, "department": "R&D", "auditorId": "A001"}'

# 采购管理 - 供应商资质审核
curl -X POST http://localhost:8081/api/business/procurement \
  -H "Content-Type: application/json" \
  -d '{"vendorId": "V001", "totalCost": 120000, "category": "equipment"}'

# 人力资源 - 员工入职审批
curl -X POST http://localhost:8081/api/business/hr \
  -H "Content-Type: application/json" \
  -d '{"employeeId": "E001", "position": "MANAGER", "department": "Sales"}'
```

#### 租户2专业版 - 智能增值服务场景
```bash
# 智能订单 - 风控检测+快速通道
curl -X POST http://localhost:8082/api/business/order \
  -H "Content-Type: application/json" \
  -d '{"amount": 3000, "itemCount": 2, "userId": "U002"}'

# 智能医疗 - AI诊断辅助
curl -X POST http://localhost:8082/api/business/medical \
  -H "Content-Type: application/json" \
  -d '{"patientId": "P002", "symptoms": ["fever", "cough"], "doctorId": "Dr002"}'

# 数据分析 - 智能洞察生成
curl -X POST http://localhost:8082/api/business/analytics \
  -H "Content-Type: application/json" \
  -d '{"dataType": "sales", "period": "monthly", "analystId": "A002"}'

# 仓储物流 - 智能库存优化
curl -X POST http://localhost:8082/api/business/warehouse \
  -H "Content-Type: application/json" \
  -d '{"warehouseId": "WH001", "operation": "restock", "items": 100}'

# 客户服务 - AI智能客服
curl -X POST http://localhost:8082/api/business/customer-service \
  -H "Content-Type: application/json" \
  -d '{"customerId": "C002", "issue": "product_inquiry", "priority": "high"}'

# 营销活动 - 精准推荐引擎
curl -X POST http://localhost:8082/api/business/marketing \
  -H "Content-Type: application/json" \
  -d '{"customerId": "C002", "campaignType": "personalized", "channel": "email"}'

# 质量管理 - 智能质检系统
curl -X POST http://localhost:8082/api/business/quality \
  -H "Content-Type: application/json" \
  -d '{"productId": "PROD001", "batchId": "B20241225", "qcType": "automated"}'
```

### 详细调用流程展示

#### 租户1调用流程 - 企业财务审批 (继承扩展模式)
```
🌐 HTTP POST /api/business/finance
    ↓
📱 UniversalController.processFinance() (tenant1-app)
    ↓ [构建BusinessContext]
🎯 BusinessContext{scenario='finance', businessType='EXPENSE_AUDIT'}
    ↓ [调用处理器]
🔧 CustomOrderProcessor.processBusiness() (customization-tenant1)
    ↓ [继承覆盖]
🎯 UniversalProcessor.processBusiness() (core-system)
    ↓ [发布BEFORE_PROCESS事件]
📡 BusinessEvent{phase='BEFORE_PROCESS', scenario='finance'}
    ↓ [钩子方法调用]
🔧 CustomOrderProcessor.beforeProcess() (定制逻辑)
    ├─ enterpriseFinanceProcessing()
    │   ├─ 💰 大额支出 ¥50000 需要CFO审批
    │   └─ 📋 财务合规：企业级财务制度验证
    ↓ [核心逻辑执行]
🎯 UniversalProcessor.processCoreLogic()
    ├─ 💼 财务业务处理
    ├─ ✅ 状态更新：PROCESSED
    ↓ [发布AFTER_PROCESS事件]
📡 BusinessEvent{phase='AFTER_PROCESS', scenario='finance'}
    ↓ [后置钩子调用]
🔧 CustomOrderProcessor.afterProcess() (企业审计)
    ├─ 📋 [企业审计] 完整操作轨迹记录
    └─ 📬 [企业通知] 多渠道 + 实时推送
✅ 返回ProcessResult{success=true, message='财务审批处理完成'}
```

#### 租户2调用流程 - 智能质检 (事件驱动模式)
```
🌐 HTTP POST /api/business/quality
    ↓
📱 UniversalController.processQuality() (tenant2-app)
    ↓ [构建BusinessContext]
🎯 BusinessContext{scenario='quality', businessType='INTELLIGENT_QC'}
    ↓ [调用处理器]
🎯 UniversalProcessor.processBusiness() (core-system)
    ↓ [发布BEFORE_PROCESS事件]
📡 BusinessEvent{phase='BEFORE_PROCESS', scenario='quality'}
    ↓ [事件监听器处理]
🔧 AdvancedOrderEventListener.handleBusinessEvent() (customization-tenant2)
    ↓ [智能验证处理]
    ├─ handleQualityValidation()
    │   ├─ 🔍 [智能质检] 机器视觉 + 自动化检测
    │   ├─ 🔬 缺陷检测精度: 99.5%+
    │   ├─ setAttribute("computer.vision", "ENABLED")
    │   └─ setAttribute("automated.inspection", "AI_POWERED")
    ↓ [核心逻辑执行]
🎯 UniversalProcessor.processCoreLogic()
    ├─ 🔍 质量检测业务处理
    ├─ ✅ 状态更新：PROCESSED
    ↓ [发布AFTER_PROCESS事件]
📡 BusinessEvent{phase='AFTER_PROCESS', scenario='quality'}
    ↓ [增值服务处理]
🔧 AdvancedOrderEventListener.handleAfterProcess()
    ├─ 🎁 增值服务: quality专属功能
    │   ├─ → 质量趋势分析
    │   └─ → 预防性维护
    ├─ 🔗 外部集成: ERP + CRM + 数据仓库
    └─ 📊 业务洞察: 实时报表 + 趋势预测
✅ 返回ProcessResult{success=true, message='智能质检处理完成'}
```

### 业务扩展能力对比

| 业务场景 | 租户1企业版 (继承扩展) | 租户2专业版 (事件驱动) |
|---------|---------------------|-------------------|
| **订单处理** | 高额审批+风险控制+企业库存策略 | 智能风控+快速通道+推荐引擎 |
| **医疗管理** | 多级认证+合规检查+医务科审批 | AI诊断辅助+药物检查+健康趋势 |
| **财务管理** | 多级财务审批+合规验证 | - |
| **采购管理** | 供应商资质审核+董事会审批 | - |
| **人力资源** | 多级人事审批+背景调查 | - |
| **数据分析** | - | 数据质量检查+ML模型验证 |
| **仓储物流** | - | 智能调度+供应链可视化 |
| **客户服务** | - | 情感分析+智能路由+满意度预测 |
| **营销活动** | - | 用户画像+精准匹配+实时个性化 |
| **质量管理** | - | 机器视觉+自动化检测+预防性维护 |

### 架构价值体现

#### 1. **场景覆盖全面**
- **企业级场景**：订单、医疗、财务、采购、HR - 深度定制
- **专业版场景**：订单、医疗、分析、仓储、客服、营销、质检 - 智能增值

#### 2. **扩展模式清晰**
- **继承扩展**：深度介入业务逻辑，完全控制处理流程
- **事件驱动**：松耦合扩展，灵活增强功能

#### 3. **调用逻辑完整**
- **统一入口**：UniversalController 支持多业务场景
- **统一上下文**：BusinessContext 承载所有业务数据
- **统一处理**：UniversalProcessor 处理所有业务逻辑
- **灵活扩展**：两种模式支撑不同定制需求
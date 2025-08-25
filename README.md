# 组件化解决方案 - 完整端到端实现

本项目展示了一个完整的**组件化架构**，支持标准化功能和租户特定定制化，使用 Spring Boot 技术栈。

## 架构概览

### 多仓库结构
- **core-system**: 核心功能模块，包含扩展点和钩子方法
- **customization-tenant1**: 租户1定制化模块（继承+事件混合模式）
- **customization-tenant2**: 租户2定制化模块（纯事件驱动模式）
- **tenant1-app**: 租户1可部署应用
- **tenant2-app**: 租户2可部署应用

### 核心设计模式

1. **扩展点模式**: 基于接口的扩展点（`OrderProcessor`）
2. **钩子方法**: 模板方法模式，支持可覆盖的钩子
3. **事件驱动架构**: Spring 事件机制实现松耦合
4. **条件化配置**: Spring Boot 条件化Bean配置
5. **插件化架构**: 模块化定制功能

## 项目结构

```
componentization/
├── core-system/                    # 核心功能模块
│   ├── src/main/java/com/company/core/
│   │   ├── model/                  # 领域模型 (Order, User)
│   │   ├── processor/              # 核心处理逻辑
│   │   ├── event/                  # Spring 事件
│   │   ├── service/                # 核心服务
│   │   └── config/                 # 配置类
│   └── pom.xml
├── customization-tenant1/          # 租户1定制化模块（高级版）
│   ├── src/main/java/com/company/tenant1/
│   │   ├── processor/              # 自定义处理器（继承模式）
│   │   ├── listener/               # 事件监听器
│   │   └── config/                 # 租户配置
│   └── pom.xml
├── customization-tenant2/          # 租户2定制化模块（企业版）
│   ├── src/main/java/com/company/tenant2/
│   │   ├── listener/               # 高级事件监听器
│   │   └── config/                 # 企业版配置
│   └── pom.xml
├── tenant1-app/                    # 租户1应用程序
│   └── src/main/java/com/company/tenant1/app/
├── tenant2-app/                    # 租户2应用程序
│   └── src/main/java/com/company/tenant2/app/
└── pom.xml                         # 父级 POM 配置
```

## 核心系统特性

### 1. 扩展点设计
```java
// 定制化核心接口
public interface OrderProcessor {
    void process(Order order);
}
```

### 2. 钩子方法
```java
// 包含钩子的模板方法
public class DefaultOrderProcessor implements OrderProcessor {
    @Override
    public void process(Order order) {
        validate(order);
        calculatePrice(order);
        beforeSave(order);        // 钩子方法
        save(order);
        beforeNotify(order);      // 钩子方法
        notifyUser(order);
    }
    
    protected void beforeSave(Order order) {} // 定制化模块可覆盖
    protected void beforeNotify(Order order) {} // 定制化模块可覆盖
}
```

### 3. 事件驱动架构
```java
// 松耦合事件机制
public class BeforeSaveEvent extends ApplicationEvent {
    private boolean skipDefaultAction = false;
    // ... 事件相关方法
}
```

## 定制化实现示例

### 租户1（高级版） - 继承+事件混合模式
- **自定义处理器**: 继承 `DefaultOrderProcessor`
- **审核逻辑**: 高额订单需要主管审核
- **短信通知**: 使用短信替代邮件通知
- **事件监听**: 额外的库存管理逻辑

### 租户2（企业版） - 纯事件驱动模式
- **复杂验证**: 大额订单的欺诈检测
- **ERP集成**: 外部系统集成
- **多渠道通知**: 邮件+短信+推送+仪表板
- **高级分析**: 报表生成

## 构建和运行

### 1. 构建所有模块
```bash
mvn clean install
```

### 2. 运行租户1应用
```bash
cd tenant1-app
mvn spring-boot:run
```

### 3. 运行租户2应用
```bash
cd tenant2-app
mvn spring-boot:run
```

## 配置管理

### 租户选择
每个应用使用 `tenant.id` 属性来激活特定的定制化功能：

```yaml
# 租户1
tenant:
  id: tenant1
  name: "高级租户"

# 租户2
tenant:
  id: tenant2
  name: "企业租户"
```

### 条件化配置
```java
@Configuration
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
@ComponentScan(basePackages = {"com.company.tenant1", "com.company.core"})
public class Tenant1Config {
}
```

## 核心优势

1. **关注点分离**: 核心逻辑与定制逻辑完全分离
2. **独立开发**: 团队可以独立开发定制化功能
3. **灵活部署**: 每个租户可以使用不同的模块组合
4. **易于维护**: 核心系统更新不影响定制化模块
5. **可扩展性**: 容易添加新租户和新的定制化功能

## 扩展方法

### 1. 继承模式（租户1）
- 继承 `DefaultOrderProcessor`
- 覆盖钩子方法
- 直接定制处理逻辑

### 2. 事件驱动（租户2）
- 监听 Spring 事件
- 松耦合设计
- 可跳过默认行为
- 更适合复杂集成场景

## 演示场景

### 租户1演示
- 常规订单处理，使用短信通知
- 高额订单需要主管审核
- 通过事件进行自定义库存更新

### 租户2演示
- 小额订单的标准处理流程
- 大额订单触发复杂验证、ERP集成和高级通知
- 多渠道通知系统

该架构为支持标准化产品和租户特定定制化提供了稳健的基础，同时保持了清晰的分离和可扩展性。
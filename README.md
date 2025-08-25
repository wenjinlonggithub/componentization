# Componentization Solution - Complete End-to-End Implementation

This project demonstrates a complete **componentization architecture** that supports both standardized functionality and tenant-specific customizations using Spring Boot.

## Architecture Overview

### Multi-Repository Structure
- **core-system**: Base functionality with extension points and hooks
- **customization-tenant1**: Premium tenant customizations (inheritance + events)
- **customization-tenant2**: Enterprise tenant customizations (event-driven only)
- **tenant1-app**: Deployable application for Tenant 1
- **tenant2-app**: Deployable application for Tenant 2

### Key Design Patterns Implemented

1. **Extension Points**: Interface-based extension points (`OrderProcessor`)
2. **Hook Methods**: Template method pattern with overridable hooks
3. **Event-Driven Architecture**: Spring events for loose coupling
4. **Conditional Configuration**: Spring Boot conditional beans
5. **Plugin Architecture**: Modular customizations

## Project Structure

```
componentization/
├── core-system/                    # Core functionality
│   ├── src/main/java/com/company/core/
│   │   ├── model/                  # Domain models (Order, User)
│   │   ├── processor/              # Core processing logic
│   │   ├── event/                  # Spring events
│   │   ├── service/                # Core services
│   │   └── config/                 # Configuration
│   └── pom.xml
├── customization-tenant1/          # Premium tenant customizations
│   ├── src/main/java/com/company/tenant1/
│   │   ├── processor/              # Custom processor (inheritance)
│   │   ├── listener/               # Event listeners
│   │   └── config/                 # Tenant config
│   └── pom.xml
├── customization-tenant2/          # Enterprise tenant customizations
│   ├── src/main/java/com/company/tenant2/
│   │   ├── listener/               # Advanced event listeners
│   │   └── config/                 # Enterprise config
│   └── pom.xml
├── tenant1-app/                    # Tenant 1 application
│   └── src/main/java/com/company/tenant1/app/
├── tenant2-app/                    # Tenant 2 application
│   └── src/main/java/com/company/tenant2/app/
└── pom.xml                         # Parent POM
```

## Core System Features

### 1. Extension Points
```java
// Core interface for customization
public interface OrderProcessor {
    void process(Order order);
}
```

### 2. Hook Methods
```java
// Template method with hooks
public class DefaultOrderProcessor implements OrderProcessor {
    @Override
    public void process(Order order) {
        validate(order);
        calculatePrice(order);
        beforeSave(order);        // Hook
        save(order);
        beforeNotify(order);      // Hook
        notifyUser(order);
    }
    
    protected void beforeSave(Order order) {} // Override in customizations
    protected void beforeNotify(Order order) {} // Override in customizations
}
```

### 3. Event-Driven Architecture
```java
// Events for loose coupling
public class BeforeSaveEvent extends ApplicationEvent {
    private boolean skipDefaultAction = false;
    // ... event methods
}
```

## Customization Examples

### Tenant 1 (Premium) - Inheritance + Events
- **Custom Processor**: Extends `DefaultOrderProcessor`
- **Audit Logic**: High-value orders require approval
- **SMS Notifications**: Uses SMS instead of email
- **Event Listeners**: Additional inventory management

### Tenant 2 (Enterprise) - Event-Driven Only
- **Complex Validation**: Fraud detection for large orders
- **ERP Integration**: External system integration
- **Multi-channel Notifications**: Email + SMS + Push + Dashboard
- **Advanced Analytics**: Report generation

## Build and Run

### 1. Build All Modules
```bash
mvn clean install
```

### 2. Run Tenant 1 Application
```bash
cd tenant1-app
mvn spring-boot:run
```

### 3. Run Tenant 2 Application
```bash
cd tenant2-app
mvn spring-boot:run
```

## Configuration Management

### Tenant Selection
Each application uses `tenant.id` property to activate specific customizations:

```yaml
# Tenant 1
tenant:
  id: tenant1
  name: "Premium Tenant"

# Tenant 2  
tenant:
  id: tenant2
  name: "Enterprise Tenant"
```

### Conditional Configuration
```java
@Configuration
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
@ComponentScan(basePackages = {"com.company.tenant1", "com.company.core"})
public class Tenant1Config {
}
```

## Key Benefits

1. **Separation of Concerns**: Core logic separated from customizations
2. **Independent Development**: Teams can work on customizations independently
3. **Flexible Deployment**: Different combinations of modules per tenant
4. **Maintainability**: Core updates don't affect customizations
5. **Scalability**: Easy to add new tenants and customizations

## Extension Approaches

### 1. Inheritance (Tenant 1)
- Extend `DefaultOrderProcessor`
- Override hook methods
- Direct customization of processing logic

### 2. Event-Driven (Tenant 2)
- Listen to Spring events
- Loose coupling
- Can skip default actions
- Better for complex integrations

## Demo Scenarios

### Tenant 1 Demo
- Regular order processing with SMS notifications
- High-value order requiring manager approval
- Custom inventory updates via events

### Tenant 2 Demo  
- Small order with standard processing
- Large order triggering complex validation, ERP integration, and advanced notifications
- Multi-channel notification system

This architecture provides a robust foundation for supporting both standardized products and tenant-specific customizations while maintaining clean separation and extensibility.
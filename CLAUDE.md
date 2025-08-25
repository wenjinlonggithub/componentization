# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Build All Modules
```bash
mvn clean install
```

### Run Specific Tenant Applications
```bash
# Run Tenant 1 Application (Premium - port 8081)
cd tenant1-app && mvn spring-boot:run

# Run Tenant 2 Application (Enterprise - port 8082)  
cd tenant2-app && mvn spring-boot:run

# Run Core System Only (port 8080)
cd core-system && mvn spring-boot:run
```

### Build Individual Modules
```bash
# Build core system
cd core-system && mvn clean install

# Build specific tenant customization
cd customization-tenant1 && mvn clean install
cd customization-tenant2 && mvn clean install
```

## Architecture Overview

This is a **componentization architecture** demonstrating multi-tenant customization patterns using Spring Boot. The system supports both standardized functionality and tenant-specific customizations through two main extension approaches.

### Multi-Module Structure

- **core-system**: Contains base functionality with extension points (`OrderProcessor` interface)
- **customization-tenant1**: Premium tenant customizations using inheritance + events hybrid approach
- **customization-tenant2**: Enterprise tenant customizations using pure event-driven approach  
- **tenant1-app/tenant2-app**: Deployable applications that combine core + tenant-specific modules

### Key Extension Patterns

#### 1. Hook Methods (Template Method Pattern)
The `DefaultOrderProcessor` class defines the main processing flow with overridable hook methods:
- `beforeSave(Order order)` - Hook for pre-save customizations
- `beforeNotify(Order order)` - Hook for pre-notification customizations

Tenant customizations can extend `DefaultOrderProcessor` and override these hooks.

#### 2. Event-Driven Extensions  
The core system publishes Spring events at key processing points:
- `BeforeSaveEvent` - Published before saving orders
- `AfterSaveEvent` - Published after successful save
- `BeforeNotifyEvent` - Published before user notification

Events support conditional execution via `skipDefaultAction()` mechanism.

#### 3. Conditional Configuration
Tenant modules are activated using `@ConditionalOnProperty`:
```java
@ConditionalOnProperty(name = "tenant.id", havingValue = "tenant1")
```

### Tenant Customization Examples

**Tenant 1 (Premium)** - Uses inheritance + events:
- `CustomOrderProcessor extends DefaultOrderProcessor` - Overrides hooks for high-value order auditing and SMS notifications
- `OrderEventListener` - Handles additional business logic via event listeners

**Tenant 2 (Enterprise)** - Uses pure event-driven:
- `AdvancedOrderEventListener` - Implements complex validation, ERP integration, and multi-channel notifications entirely through event handling

### Configuration Management

Each tenant application uses `tenant.id` property to activate specific customizations:
- `tenant.id=tenant1` activates Tenant1Config and related beans
- `tenant.id=tenant2` activates Tenant2Config and related beans

The core system uses `@ConditionalOnMissingBean(OrderProcessor.class)` to provide default implementation when no tenant-specific processor is available.

### Development Workflow

When adding new tenant customizations:
1. Create new customization module following existing patterns
2. Choose extension approach: inheritance (direct hook overrides) or event-driven (loose coupling)
3. Add conditional configuration with unique `tenant.id` value  
4. Create deployable application module that includes core + customization dependencies
5. Configure application.yml with appropriate tenant.id and other tenant-specific properties

### Important Implementation Details

- Events are published synchronously and support conditional skipping of default behavior
- The `@Order` annotation controls event listener execution sequence
- Tenant modules depend only on core-system, avoiding cross-tenant dependencies
- Each deployable application runs on different ports (8081, 8082) for independent deployment
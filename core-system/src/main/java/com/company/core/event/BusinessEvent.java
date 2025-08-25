package com.company.core.event;

import com.company.core.model.BusinessContext;
import org.springframework.context.ApplicationEvent;

/**
 * 通用业务事件 - 统一事件机制
 * 
 * 兼容原有事件系统，增加BusinessContext支持
 */
public class BusinessEvent extends ApplicationEvent {
    
    private final BusinessContext context;
    private final String phase;
    private boolean skipDefaultAction = false;

    public BusinessEvent(Object source, BusinessContext context, String phase) {
        super(source);
        this.context = context;
        this.phase = phase;
    }

    public BusinessContext getContext() { return context; }
    public String getPhase() { return phase; }
    
    public void skipDefaultAction() { this.skipDefaultAction = true; }
    public boolean isSkipDefaultAction() { return skipDefaultAction; }

    // 便捷方法
    public boolean isForScenario(String scenario) {
        return scenario.equals(context.getScenario());
    }

    public boolean isForBusinessType(String businessType) {
        return businessType.equals(context.getBusinessType());
    }

    public boolean isForPhase(String phase) {
        return phase.equals(this.phase);
    }

    @Override
    public String toString() {
        return String.format("BusinessEvent{scenario='%s', businessType='%s', phase='%s'}", 
                           context.getScenario(), context.getBusinessType(), phase);
    }
}
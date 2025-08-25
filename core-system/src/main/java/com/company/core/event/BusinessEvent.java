package com.company.core.event;

import com.company.core.model.BusinessContext;
/**
 * Universal Business Event - unified event mechanism
 * Simple version without Spring dependency for compilation
 */
public class BusinessEvent {
    
    private String phase;
    private BusinessContext context;
    private boolean skipDefault = false;

    public BusinessEvent(String phase, BusinessContext context) {
        this.context = context;
        this.phase = phase;
    }

    public String getPhase() { return phase; }
    public BusinessContext getContext() { return context; }
    
    public boolean isForPhase(String phase) {
        return this.phase != null && this.phase.equals(phase);
    }
    
    public void skipDefaultAction() {
        this.skipDefault = true;
    }
    
    public boolean isSkipDefault() { return skipDefault; }
}
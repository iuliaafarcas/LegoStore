package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Order(2)
@Component
public class PerformanceMonitoringAspect {
    @Pointcut("execution(public * org.example.service.LegoStoreService.*(..)) && !within(PerformanceMonitoringAspect)")
    private void performanceMonitoring() {
    }

    private static final Logger logger = Logger.getLogger(PerformanceMonitoringAspect.class.getName());

    @Around("performanceMonitoring()")
    public Object monitorPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long timeDuration = endTime - startTime;
            logger.info("Operation duration for " + pjp.getSignature().getName() + ": " + timeDuration + "ms");
        }
    }
}

package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Order(1)
@Component
public class LoggingAspect {

    @Pointcut("execution(public * org.example.service.LegoStoreService.*(..)) && !within(LoggingAspect)")
    private void logging() {
    }

    private static final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Before("logging()")
    public void doLogBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info(buildLog("entered", methodSignature, joinPoint));
    }


    @AfterReturning("logging()")
    public void doLogAfterReturning(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.info(buildLog("exited successfully", methodSignature, joinPoint));
    }

    @AfterThrowing(pointcut= "logging()", throwing = "ex")
    public void doLogAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        logger.severe(methodSignature.getName() + "(): Error " + ex.getMessage());
    }

    String buildLog(String methodOperation, MethodSignature methodSignature, JoinPoint joinPoint) {

        StringBuilder log = new StringBuilder("METHOD " + methodSignature.getName() + ": " + methodOperation + " ");
        for (int i = 0; i < methodSignature.getParameterNames().length; i++) {
            log.append(methodSignature.getParameterNames()[i]).append(" ").append(joinPoint.getArgs()[i].toString()).append("; ");

        }
        return log.toString();
    }
}

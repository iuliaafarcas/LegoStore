package org.example.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(-1)
public class AuthorizationAspect {

    @Pointcut("@annotation(RequiresRole) && !within(AuthorizationAspect)")
    public void roleRequired(){}

    @Before("roleRequired()")
    public void authorize(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthorizationServiceException("Not authenticated!");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RequiresRole roleRequiredAnnotation = method.getAnnotation(RequiresRole.class);
        String requiredRole = roleRequiredAnnotation.value();

        boolean userHasRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + requiredRole));
        if (!userHasRole) {
            throw new AuthorizationServiceException("You don't have the required role!");
        }
    }

}

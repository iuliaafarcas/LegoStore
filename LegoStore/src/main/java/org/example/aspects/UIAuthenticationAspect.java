package org.example.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Aspect
@Component
@Order(-2)
public class UIAuthenticationAspect {

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

    @Pointcut("(@annotation(RequiresAuthentication) || @annotation(RequiresRole)) && !within(UIAuthenticationAspect)")
    public void authenticationRequired(){}

    @Before("authenticationRequired()")
    public void authenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return;
        }
        Authentication authenticationToken = getAuthenticationFromUI();
        authentication = customAuthenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthenticationFromUI() {
        try {
            String username = JOptionPane.showInputDialog("Username:");
            String password = JOptionPane.showInputDialog("Password:");
            return new UsernamePasswordAuthenticationToken(username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


}

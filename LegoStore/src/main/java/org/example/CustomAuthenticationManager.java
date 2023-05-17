package org.example;

import org.example.model.Client;
import org.example.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final ClientRepository clientRepository;

    public CustomAuthenticationManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        Client client = clientRepository.findClientByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username or password wrong"));

        if (!client.getPassword().equals(password)) {
            throw new BadCredentialsException("Username or password wrong");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (client.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + client.getRole()));
        }
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }
}

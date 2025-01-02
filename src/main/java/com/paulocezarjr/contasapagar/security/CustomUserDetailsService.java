package com.paulocezarjr.contasapagar.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("$2a$10$X4T4z3j1STyFTF4Gvho1l.P6oG3QcAeX/kVhsS9sb9JzAj1m.GlaW") // senha: admin123
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}

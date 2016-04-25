package com.openu.security;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.openu.model.User;
import com.openu.repository.AdministratorRepository;
import com.openu.repository.CustomerRepository;

@Service
public class AuthProvider implements AuthenticationProvider {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private AdministratorRepository administratorRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        Authentication res = isValid(authentication);
        if (!res.isAuthenticated()) {
            throw new BadCredentialsException("Bad credentials");
        }
        return res;
    }

    private Authentication isValid(final Authentication authentication) {
        Authentication res = authentication;
        User user = administratorRepository.findByUsername(String.valueOf(authentication.getPrincipal()));
        if (user == null) {
            user = customerRepository.findByUsername(String.valueOf(authentication.getPrincipal()));
        }
        if (user != null && user.getPassword().equals(authentication.getCredentials())) {
            res = createSuccessAuthentication(authentication, user);
        }
        return res;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, User user) {
        final UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(),
                loadAuthorities(user));
        result.setDetails(authentication.getDetails());
        return result;
    }

    private static Collection<GrantedAuthority> loadAuthorities(User user) {
        return user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toList());
    }
}
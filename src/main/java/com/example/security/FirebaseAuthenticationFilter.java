package com.example.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                String uid = decodedToken.getUid();
                String role = (String) decodedToken.getClaims().get("role");

                List<GrantedAuthority> authorities = getAuthoritiesFromToken(decodedToken);

                SecurityContextHolder.getContext()
                        .setAuthentication(
                                new FirebaseAuthenticationToken(token, decodedToken, authorities));

                SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
            } catch (Exception e) {
                System.out.println("Invalid Firebase token: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    private static List<GrantedAuthority> getAuthoritiesFromToken(FirebaseToken token) {
        Object claims = token.getClaims().get("role");
        List<String> permissions = new ArrayList<>();
        permissions.add((String) claims);
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        if (permissions != null && !permissions.isEmpty()) {
            authorities = AuthorityUtils.createAuthorityList(permissions);
        }
        return authorities;
    }
}



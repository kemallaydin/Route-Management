package com.kemalaydin.routemanagement.security.configuration;

import com.kemalaydin.routemanagement.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        Claims claims = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            claims = jwtUtil.getClaims(jwt);
        }

        if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> roles = claims.get("roles", List.class);

            List<GrantedAuthority> authorities = CollectionUtils.isEmpty(roles)
                    ? Collections.emptyList()
                    : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UserDetails userDetails = User.builder()
                    .username(claims.getSubject())
                    .password("")
                    .authorities(authorities)
                    .build();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}

package org.bashirov.cashdeposit.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    public JwtProvider jwtProvider;

    @Override
    public void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = JwtProvider.getTokenFromRequest(httpRequest);
        String method = httpRequest.getMethod();

        if (jwtProvider.validateToken(token)) {
            log.info("Token {} is valid", token);
            String uri = httpRequest.getRequestURI();
            long userIdFromToken = jwtProvider.getUserIdFromToken(token);
            long userIdFromRequest = getUserIdFromHttpRequestUri(uri);

            if ((method.equals("DELETE") || method.equals("PATCH")
                    || uri.contains("/profiles/create") || uri.contains("/phones/create"))
                    && (userIdFromToken == userIdFromRequest)) {
                GrantedAuthority authority = new SimpleGrantedAuthority("USER");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userIdFromToken,
                        null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }

    private long getUserIdFromHttpRequestUri(String uri) {
        log.info("Changing data for uri = {}", uri);

        int lastSlashIndexOfUri = uri.lastIndexOf('/') + 1;
        long userId = -1L;
        try {
            userId = Long.parseLong(uri.substring(lastSlashIndexOfUri));
        } catch (Exception e) {
            log.error("Wrong or empty userId in uri = {}", uri);
        }

        log.info("Changing data of userId = {}", userId);
        return userId;
    }
}
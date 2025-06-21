package br.com.questionarium.user_service.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import br.com.questionarium.user_service.security.jwt.JwtTokenDecoder;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RequestMatcher protectedEndpoints;
    private final JwtTokenDecoder tokenDecoder;

    public JwtAuthenticationFilter(RequestMatcher protectedEndpoints,
            JwtTokenDecoder tokenDecoder) {
        this.protectedEndpoints = protectedEndpoints;
        this.tokenDecoder = tokenDecoder;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // pula este filter se NÃO for endpoint protegido
        return !protectedEndpoints.matches(request);
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                Long userId = tokenDecoder.getCurrentUserId();
                List<String> roles = tokenDecoder.getRoles();
                Collection<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
package org.ufpr.questionarium.filters;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

@Service
public class AngularRoutingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        val mode = request.getHeader("Sec-Fetch-Mode");
        if (mode.equals("navigate")) {
            val rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    };
}

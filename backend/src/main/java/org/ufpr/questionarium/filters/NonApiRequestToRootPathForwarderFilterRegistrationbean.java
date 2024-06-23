package org.ufpr.questionarium.filters;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro adicionado ao FilterChain para redirecionar todos os requests que não começam com /api ou /resources para o angular.
// O filtro intercepta o request e confere se a url da requisição começa com /api ou /resources(caminho para arquivos static)
// caso a url começe com outro padrão redireciona para o caminho '/'

@Service
public class NonApiRequestToRootPathForwarderFilterRegistrationbean extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api") && !request.getRequestURI().equals("/")
                && !request.getRequestURI().startsWith("/resources")) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/");
            requestDispatcher.forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
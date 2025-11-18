package com.delivery_api.Projeto.Delivery.API.security;

import com.delivery_api.Projeto.Delivery.API.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (Usuario) authentication.getPrincipal();
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public String getCurrentUserRole() {
        return getCurrentUser().getRole().name();
    }

    public Long getCurrentRestauranteId() {
        Usuario usuario = getCurrentUser();
        return usuario.getRestauranteId();
    }

    public boolean hasRole(String role) {
        try {
            Usuario usuario = getCurrentUser();
            return usuario.getRole().name().equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isCliente() {
        return hasRole("CLIENTE");
    }

    public boolean isRestaurante() {
        return hasRole("RESTAURANTE");
    }

    public boolean isOwnerCliente(Long clienteId) {
        Usuario user = getCurrentUser();

        if (isAdmin()) return true;

        return user.getId().equals(clienteId);
    }

    public boolean isOwnerEmail(String email) {
        Usuario user = getCurrentUser();

        if (isAdmin()) return true;

        return user.getEmail().equalsIgnoreCase(email);
    }
}

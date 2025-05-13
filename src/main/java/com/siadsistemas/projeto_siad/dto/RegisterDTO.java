package com.siadsistemas.projeto_siad.dto;

import com.siadsistemas.projeto_siad.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}

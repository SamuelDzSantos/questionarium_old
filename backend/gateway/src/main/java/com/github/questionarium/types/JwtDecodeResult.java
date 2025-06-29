package com.github.questionarium.types;

import lombok.Builder;

@Builder
public record JwtDecodeResult(Long userId,
        Boolean isAdmin) {
}

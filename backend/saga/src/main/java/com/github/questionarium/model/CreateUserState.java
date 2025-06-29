package com.github.questionarium.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("create_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserState {
    @Id
    Long saga_id;
    Long userId;
    String confirmationUrl;
}

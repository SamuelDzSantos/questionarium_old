package com.github.questionarium.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash("patch_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchUserState {
    @Id
    Long saga_id;
    Long userId;
}

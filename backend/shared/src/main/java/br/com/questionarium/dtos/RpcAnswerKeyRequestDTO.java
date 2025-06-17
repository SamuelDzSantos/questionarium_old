package br.com.questionarium.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcAnswerKeyRequestDTO {
        private List<Long> questionIds;
        private Long userId;
}

package br.com.questionarium.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcQuestionDTO {
        private Long userId;
        private Long question;
        private Boolean multipleChoice;
        private Integer numberLines;
        private String educationLevel;
        private String header;
        private String headerImage;
        private Long answerId;
        private Boolean enable;
        private String accessLevel;
        private List<String> tags;
        private List<RpcAlternativeDTO> alternatives;
        private Double weight;
}

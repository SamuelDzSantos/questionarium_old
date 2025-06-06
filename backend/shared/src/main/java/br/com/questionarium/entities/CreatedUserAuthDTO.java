// shared/src/main/java/br/com/questionarium/entities/CreatedUserAuthDTO.java
package br.com.questionarium.entities;

/**
 * DTO enviado pelo user-service ao auth-service via RabbitMQ, contendo
 * apenas os dados que o auth precisa para criar credenciais.
 */
public record CreatedUserAuthDTO(
        String login,
        String password,
        String role
) {
}

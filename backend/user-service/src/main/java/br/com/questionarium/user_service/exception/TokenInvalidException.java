package br.com.questionarium.user_service.exception;

/**
 * Exceção lançada quando o token de verificação de e-mail
 * é inválido ou expirou.
 */
public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String message) {
        super(message);
    }
}

package org.ufpr.questionarium.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*  Classe utilizada para armazenar o par de chaves pública e privada presentes no diretório certs. Os arquivos são mapeados para essa classe
através dos caminhos configurados no application.properties com o prefixo 'rsa' */

@Configuration
@ConfigurationProperties(prefix = "rsa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RsaKeyProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}

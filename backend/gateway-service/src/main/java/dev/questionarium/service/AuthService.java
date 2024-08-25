package dev.questionarium.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.questionarium.entities.AuthRequest;
import dev.questionarium.entities.AuthResponse;
import dev.questionarium.entities.PasswordPatch;
import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.types.AccessType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

        private final RestTemplate template;
        private final AuthenticationManager authenticationManager;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtils jwtUtils;

        public boolean register(RegistrationRequest request) {

                RegistrationRequest passwordEncodedRequest = new RegistrationRequest(request.name(), request.email(),
                                passwordEncoder.encode(request.password()), request.role());

                template.postForObject("http://user-service/users", passwordEncodedRequest,
                                UserData.class);
                return true;
        }

        public AuthResponse login(AuthRequest request) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                request.login(), request.password());

                this.authenticationManager.authenticate(authenticationToken);

                UserData userData = template.getForObject("http://user-service/users?email={email}", UserData.class,
                                request.login());

                if (userData == null) {
                        throw new RuntimeException("Usuário nulo!");
                }

                String accessToken = jwtUtils.generate(userData.email(), AccessType.ACCESS);

                String refreshKey = jwtUtils.generate(userData.email(), AccessType.REFRESH);

                return new AuthResponse(accessToken, refreshKey, userData);

        }

        public UserData getCurrent(Object principal) {

                Jwt jwt = (Jwt) principal;

                UserData userData = template.getForObject("http://user-service/users?email={email}", UserData.class,
                                jwt.getSubject());
                return userData;

        }

        public String getPasswordToken(String email) {
                String token = template.getForObject("http://user-service/users/forgot-password?email={email}",
                                String.class, email);
                return token;
        }

        public Boolean updatePassword(PasswordPatch patch) {
                PasswordPatch encodedPasswordPatch = new PasswordPatch(this.passwordEncoder.encode(patch.password()),
                                patch.token(), patch.code());
                System.out.println(patch.password());
                System.out.println("HI");
                Boolean result = template.patchForObject("http://user-service/users/update-password",
                                encodedPasswordPatch,
                                Boolean.class);
                return result;
        }

}

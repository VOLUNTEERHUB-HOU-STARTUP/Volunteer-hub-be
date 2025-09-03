package com.example.VolunteerHub.configuration;

import com.example.VolunteerHub.dto.request.IntrospectRequest;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Duration;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${app.jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${spring.security.oauth2.client.provider.google.jwk-set-uri}")
    private String googleJwkSetUri;

    @Value("${spring.security.oauth2.client.provider.google.issuer-uri}")
    private String googleIssuerUri;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        if (isGoogleToken(token))
            return googleDecoder().decode(token);

        try {
            // kiểm tra token bằng introspection: ktra token có hợp lệ hay k
            var response = authenticationService.introspect(
                    IntrospectRequest.builder()
                            .token(token)
                            .build()
            );

            if (!response.isValid()) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");

            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }

    private JwtDecoder googleDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withJwkSetUri(googleJwkSetUri)
                .build();

        OAuth2TokenValidator<Jwt> oAuth2TokenValidator = JwtValidators.createDefaultWithIssuer(googleIssuerUri);

        OAuth2TokenValidator<Jwt> withSkew = new DelegatingOAuth2TokenValidator<>(
                oAuth2TokenValidator,
                new JwtTimestampValidator(Duration.ofMinutes(5))
        );

        jwtDecoder.setJwtValidator(withSkew);

        return jwtDecoder;
    }

    private boolean isGoogleToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3)
                return false;

            String headerJson = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));

            return headerJson.contains("\"alg\":\"RS256\"") ||
                    headerJson.contains("\"kid\"") ||
                    headerJson.contains("\"alg\":\"RS");
        } catch (Exception e) {
            return false;
        }
    }
}

package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.AuthenticationRequest;
import com.example.VolunteerHub.dto.request.IntrospectRequest;
import com.example.VolunteerHub.dto.request.LogoutRequest;
import com.example.VolunteerHub.dto.request.RefreshRequest;
import com.example.VolunteerHub.dto.response.AuthenticationResponse;
import com.example.VolunteerHub.dto.response.IntrospectResponse;
import com.example.VolunteerHub.entity.InvalidatedToken;
import com.example.VolunteerHub.entity.Users;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.repository.InvalidTokenRepository;
import com.example.VolunteerHub.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    InvalidTokenRepository invalidatedTokenRepository;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException | JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid) // đã kiểm tra nếu lỗi trả trong hàm verifyToken()
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ?
                new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()) :
                signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expiryTime.after(new Date())) )
            throw new AppException(ErrorCode.UNAUTHORIZED);

        // lấy ra jwt id từ token xong ktra xem token có trong db token bị vô hiệu k
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return signedJWT;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.PASSWORD_INVALID);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private String generateToken(Users users) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getEmail())
                .issuer("ducminh")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void logout(LogoutRequest request) {
        try {
            var jwtToken = verifyToken(request.getToken(), true);
            String jti = jwtToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = jwtToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var jwtToken = verifyToken(request.getToken(), true);
        String jti = jwtToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = jwtToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = jwtToken.getJWTClaimsSet().getSubject(); // username = email
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }
}

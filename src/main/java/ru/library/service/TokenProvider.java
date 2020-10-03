package ru.library.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.library.config.ApplicationProperties;
import ru.library.domain.Token;
import ru.library.domain.User;
import ru.library.domain.dto.LoginDTO;
import ru.library.domain.dto.RefreshDTO;
import ru.library.domain.response.ErrorCode;
import ru.library.exception.ClientErrorException;
import ru.library.repository.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static ru.library.util.Constants.PASSWORD_SALT;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final ApplicationProperties applicationProperties;
    private final OctetKeyPair octetKeyPair;
    private final UserRepository userRepository;
    @Qualifier("passwordEncoder")
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates access and refresh tokens
     * @param dto - contains username and password
     * @return - tokens
     * @throws ClientErrorException
     */
    public Token createTokens(LoginDTO dto) throws ClientErrorException {

        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());

        if (!optionalUser.isPresent()) {
            log.error("User was not found. Username: {}", dto.getUsername());
            throw new ClientErrorException(ErrorCode.CREDENTIALS_ARE_INVALID,
                    "Credentials are invalid", "Credentials are invalid");
        }

        User user = optionalUser.get();

        boolean isPasswordCorrect = passwordEncoder.matches(PASSWORD_SALT + dto.getPassword(), user.getPassword());

        if (!isPasswordCorrect) {
            log.error("Password is not correct. Username: {}", dto.getUsername());
            throw new ClientErrorException(ErrorCode.CREDENTIALS_ARE_INVALID,
                    "Credentials are invalid", "Credentials are invalid");
        }

        return getTokens(user);
    }

    /**
     * Refreshes access and refresh tokens
     * @param dto - Should contain valid refresh token
     * @return - tokens
     * @throws ClientErrorException
     */
    public Token refreshToken(RefreshDTO dto) throws ClientErrorException {
        Boolean isValid = validateToken(dto.getRefreshToken());

        if (!isValid) {
            throw new ClientErrorException(ErrorCode.JWT_TOKEN_EXPIRED,
                    "Refresh token is expired", "Refresh token is expired");
        }

        JWTClaimsSet jwtClaimsSet = null;
        try {
            jwtClaimsSet = SignedJWT.parse(dto.getRefreshToken()).getJWTClaimsSet();
        } catch (ParseException e) {
            log.error("Refresh token. ParseException", e);
            throw new ClientErrorException(ErrorCode.INTERNAL_ERROR,
                    "Refresh token. Parsing error", "Refresh token. Parsing error");
        }

        String userId = jwtClaimsSet.getSubject();

        Optional<User> optionalUser = userRepository.findById(Long.valueOf(userId));

        if (!optionalUser.isPresent()) {
            throw new ClientErrorException(ErrorCode.USER_NOT_FOUND,
                    String.format("User was not found. User Id: %s", userId),
                    String.format("User was not found: User Id: %s", userId));
        }

        return getTokens(optionalUser.get());
    }

    /**
     * Validates token
     * @param jwt - Token to verify
     * @return - true if token is valid, false if not
     * @throws ClientErrorException
     */
    public Boolean validateToken(String jwt) throws ClientErrorException {
        Boolean isValid;
        try {
            isValid = SignedJWT.parse(jwt)
                    .verify(new Ed25519Verifier(octetKeyPair.toPublicJWK())); // validate signature
        } catch (ParseException e) {
            log.error("Token Validation. ParseException", e);
            throw new ClientErrorException(ErrorCode.INVALID_JWT_TOKEN,
                    "Token is not valid", "Token is not valid");
        } catch (JOSEException e) {
            log.error("Token Validation. Javascript Object Signing and Encryption (JOSE) exception", e);
            throw new ClientErrorException(ErrorCode.INVALID_JWT_TOKEN,
                    "Token is not valid", "Token is not valid");
        }
        return isValid;
    }

    private Token getTokens(User user) throws ClientErrorException {
        JWSHeader header = getJwsHeader();

        SignedJWT accessSignedJWT = getSignedJwt(header,
                user,
                applicationProperties.getAccessTokenTtlInSeconds());

        try {
            accessSignedJWT.sign(new Ed25519Signer(octetKeyPair));
        } catch (JOSEException e) {
            log.error("Access token. Javascript Object Signing and Encryption (JOSE) exception", e);
            throw new ClientErrorException(ErrorCode.INTERNAL_ERROR,
                    "Access token signing error", "Access token signing error");
        }

        String accessToken = accessSignedJWT.serialize();

        SignedJWT refreshSignedJWT = getSignedJwt(header,
                user,
                applicationProperties.getRefreshTokenTtlInSeconds());

        try {
            refreshSignedJWT.sign(new Ed25519Signer(octetKeyPair));
        } catch (JOSEException e) {
            log.error("Refresh token. Javascript Object Signing and Encryption (JOSE) exception", e);
            throw new ClientErrorException(ErrorCode.INTERNAL_ERROR,
                    "Refresh token signing error", "Refresh token signing error");
        }

        String refreshToken = refreshSignedJWT.serialize();

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationInSeconds(applicationProperties.getAccessTokenTtlInSeconds()).build();
    }

    private JWSHeader getJwsHeader() {
        return new JWSHeader.Builder(JWSAlgorithm.EdDSA)
                .type(JOSEObjectType.JWT)
                .keyID(octetKeyPair.getKeyID())
                .build();
    }

    private SignedJWT getSignedJwt(JWSHeader header, User user, Long ttlInSeconds) {
        JWTClaimsSet payload = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .expirationTime(Date.from(Instant.now().plusSeconds(ttlInSeconds)))
                .build();

        return new SignedJWT(header, payload);
    }
}

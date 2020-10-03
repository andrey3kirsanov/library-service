package ru.library.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfiguration {

    /**
     * Key pair that we use to sign tokens
     * @return Octet key pair
     * @throws JOSEException
     */
    @Bean
    public OctetKeyPair octetKeyPair() throws JOSEException {
        return new OctetKeyPairGenerator(Curve.Ed25519).algorithm(JWSAlgorithm.EdDSA)
                .keyUse(KeyUse.SIGNATURE)
                .keyID("library-auth-key-id")
                .generate();
    }
}

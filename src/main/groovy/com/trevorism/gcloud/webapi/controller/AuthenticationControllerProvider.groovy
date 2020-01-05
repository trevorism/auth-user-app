package com.trevorism.gcloud.webapi.controller

import com.auth0.AuthenticationController
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder

class AuthenticationControllerProvider {

    static AuthenticationController provide() {
        Properties properties = new Properties()
        properties.load(AuthenticationController.class.getClassLoader().getResourceAsStream("secrets.properties") as InputStream)

        String domain = properties.domain
        JwkProvider jwkProvider = new JwkProviderBuilder(domain).build()
        return AuthenticationController.newBuilder(domain, properties.clientId, properties.clientSecret)
                .withJwkProvider(jwkProvider)
                .build()
    }
}

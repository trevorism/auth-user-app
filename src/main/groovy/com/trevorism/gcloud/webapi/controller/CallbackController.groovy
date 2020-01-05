package com.trevorism.gcloud.webapi.controller

import com.auth0.SessionUtils
import com.auth0.Tokens
import io.swagger.annotations.Api

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import java.util.logging.Logger

@Api("Callback Operations")
@Path("/callback")
class CallbackController {

    private static final Logger log = Logger.getLogger(CallbackController.class.name)

    @GET
    Response handle(@Context HttpServletRequest httpServletRequest){
        try {
            Tokens tokens = AuthenticationControllerProvider.provide().handle(httpServletRequest)

            log.info("Access token: ${tokens.getAccessToken()}")
            log.info("ID token: ${tokens.getIdToken()}")

            SessionUtils.set(httpServletRequest, "accessToken", tokens.getAccessToken())
            SessionUtils.set(httpServletRequest, "idToken", tokens.getIdToken())
            return Response.temporaryRedirect (new URI("/")).build()

        } catch (Exception e) {
            log.warning("Error with authentication ${e.message}")
            return Response.temporaryRedirect (new URI("/")).build()
        }
    }
}

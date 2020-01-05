package com.trevorism.gcloud.webapi.controller

import io.swagger.annotations.Api

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

import com.auth0.AuthenticationController

import java.util.logging.Logger

@Api("Login Operations")
@Path("/login")
class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class.name)

    @GET
    Response login(@Context HttpServletRequest httpServletRequest){
        AuthenticationController authenticationController = AuthenticationControllerProvider.provide()
        String url = authenticationController.buildAuthorizeUrl(httpServletRequest, getRedirectUrl(httpServletRequest)).build()
        log.info("Authorization url: $url" )
        Response.temporaryRedirect(new URI("$url")).build()
    }

    private String getRedirectUrl(HttpServletRequest req){
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            redirectUri += ":" + req.getServerPort()
        }
        redirectUri += "/api/callback"
        return redirectUri
    }
}

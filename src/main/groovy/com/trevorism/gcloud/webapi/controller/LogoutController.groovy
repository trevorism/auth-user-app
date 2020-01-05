package com.trevorism.gcloud.webapi.controller

import io.swagger.annotations.Api

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Api("Logout Operations")
@Path("/logout")
class LogoutController {

    @GET
    Response logout(@Context HttpServletRequest httpServletRequest){
        httpServletRequest.getSession()?.invalidate()
        Response.temporaryRedirect(new URI("/")).build()
    }
}

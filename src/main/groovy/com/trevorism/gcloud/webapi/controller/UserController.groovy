package com.trevorism.gcloud.webapi.controller

import com.auth0.SessionUtils
import com.trevorism.gcloud.webapi.model.User
import io.swagger.annotations.Api

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.Path
import javax.ws.rs.core.Context

@Api("User Operations")
@Path("user")
class UserController {

    @GET
    User getUser(@Context HttpServletRequest req) {
        final String accessToken = (String) SessionUtils.get(req, "accessToken")
        final String idToken = (String) SessionUtils.get(req, "idToken")

        if(!accessToken || !idToken)
            throw new NotAuthorizedException("Need to login")

        User user = new User(accessToken: accessToken, idToken: idToken)
        return user
    }

}

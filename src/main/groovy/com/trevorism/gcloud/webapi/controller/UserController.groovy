package com.trevorism.gcloud.webapi.controller

import com.auth0.AuthenticationController
import com.auth0.SessionUtils
import com.google.gson.Gson
import com.trevorism.gcloud.webapi.model.Oauth2Response
import com.trevorism.gcloud.webapi.model.User
import com.trevorism.gcloud.webapi.model.UserInfoResponse
import com.trevorism.http.HttpClient
import com.trevorism.http.headers.HeadersBlankHttpClient
import com.trevorism.http.headers.HeadersHttpClient
import com.trevorism.http.headers.HeadersJsonHttpClient
import com.trevorism.http.util.ResponseUtils
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


    //This is how to get the user information from the userId, which can be retrieved from decoding the idToken
    //Or calling user info using the access token (shown below)
    static void main(String [] args){
        HeadersHttpClient httpClient = new HeadersJsonHttpClient()
        Gson gson = new Gson()

        Properties properties = new Properties()
        properties.load(AuthenticationController.class.getClassLoader().getResourceAsStream("secrets.properties") as InputStream)

        Oauth2Response response = getManagementApiToken(properties, gson)
        String json = retrieveUserData(httpClient, gson, properties, response)

        println json


    }

    private static String retrieveUserData(HeadersJsonHttpClient httpClient, Gson gson, Properties properties, Oauth2Response response) {
        UserInfoResponse uir = obtainUserId(httpClient, gson)
        String userId = URLEncoder.encode(uir.sub, "UTF-8")
        String json = ResponseUtils.getEntity httpClient.get("https://${properties.domain}/api/v2/users/$userId", ["authorization": "bearer ${response.access_token}".toString()])
        json
    }

    private static Oauth2Response getManagementApiToken(Properties properties, Gson gson) {
        HeadersHttpClient blankClient = new HeadersBlankHttpClient()
        String result = ResponseUtils.getEntity blankClient.post("https://${properties.domain}/oauth/token", "grant_type=client_credentials&client_id=${properties.clientId}&client_secret=${properties.clientSecret}&audience=https%3A%2F%2F${properties.domain}%2Fapi%2Fv2%2F", ["content-type": "application/x-www-form-urlencoded"])
        Oauth2Response response = gson.fromJson(result, Oauth2Response)
        response
    }

    private static UserInfoResponse obtainUserId(HeadersJsonHttpClient httpClient, Gson gson) {
        String accessToken = "LZRXdRHUo5v_D2aMdR-fd995OOOpEDga"
        String json = ResponseUtils.getEntity httpClient.get("https://trevorism.auth0.com/userinfo", ["Authorization": "Bearer ${accessToken}".toString()])
        UserInfoResponse uir = gson.fromJson(json, UserInfoResponse)
        uir
    }
}

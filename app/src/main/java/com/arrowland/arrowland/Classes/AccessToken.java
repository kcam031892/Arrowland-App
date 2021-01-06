package com.arrowland.arrowland.Classes;

import com.squareup.moshi.Json;

/**
 * Created by Mhack Bautista on 9/8/2018.
 */

public class AccessToken {

    @Json(name = "token_type")
    String tokenType;
    @Json(name ="expires in")
    int expiresIn;
    @Json(name = "access_token")
    String accessToken;


}

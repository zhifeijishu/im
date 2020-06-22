package com.tksflysun.hi.net;

import com.tksflysun.hi.bean.Friend;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.bean.res.ApiResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
   static final String BASE_URL="https://tksflysun.com/im/";
    @POST("api/authentication/loginByMobile")
    Observable<ApiResponse<User>> login(@Body RequestBody requestBody);
    @POST("api/user/registByMobile")
    Observable<ApiResponse<User>> register(@Body RequestBody requestBody);
    @POST("api/friend/friends")
    Observable<ApiResponse<List<Friend>>> getFriends( );
}

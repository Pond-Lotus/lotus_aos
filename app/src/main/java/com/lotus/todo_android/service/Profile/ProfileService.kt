package com.lotus.todo_android.service.Profile

import com.lotus.todo_android.Data.Modify.ChangePassword
import com.lotus.todo_android.Data.Profile.AuthCode
import com.lotus.todo_android.Data.Profile.Login
import com.lotus.todo_android.Data.Profile.Register
import com.lotus.todo_android.response.ModifyResponse.ChangePasswordResponse
import com.lotus.todo_android.response.ModifyResponse.ChangeProfileResponse
import com.lotus.todo_android.response.ModifyResponse.DeleteProfileImageResponse
import com.lotus.todo_android.response.ProfileResponse.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {

    @POST("/account/login/")
    fun requestLogin(
        @Body loginRequest: com.lotus.todo_android.Data.Profile.Login
    ) : Call<LoginResponse>

    @POST("/account/logout/")
    fun requestLogout(
        @Header("Authorization") token: String
    ) : Call<LogoutResponse>

    @GET("/account/emailcode/")
    fun requestEmail(
        @Query("email") email: String
    ) : Call<AuthEmailResponse>

    @POST("/account/emailcode/")
    fun requestCode(
        @Body authCodeRequest: com.lotus.todo_android.Data.Profile.AuthCode
    ) : Call<AuthCodeResponse>

    @POST("/account/register/")
    fun requestRegister(
        @Body registerRequest: com.lotus.todo_android.Data.Profile.Register
    ) : Call<RegisterResponse>

    @DELETE("/account/withdrawal/")
    fun requestDeleteAccount(
        @Header("Authorization") token: String
    ): Call<DeleteAccountResponse>

    @GET("/account/findpw/")
    fun requestFindOutPassWord(
        @Query("email") email: String
    ) : Call<SearchEmailResponse>

    @POST("/account/edit2/")
    fun requestChangePassword(
        @Header("Authorization") token: String,
        @Body changePasswordRequest: com.lotus.todo_android.Data.Modify.ChangePassword
    ): Call<ChangePasswordResponse>

    @Multipart
    @POST("/account/edit1/")
    fun requestChangeProfile(
        @Header("Authorization") token: String,
        @Part ("imdel") imdel: Boolean,
        @Part ("nickname") nickname: RequestBody,
        @Part image: MultipartBody.Part
    ) : Call<ChangeProfileResponse>

    @Multipart
    @POST("/account/edit1/")
    fun requestDeleteProfileImage(
        @Header("Authorization") token: String,
        @Part("nickname") nickname: RequestBody,
        @Part("imdel") imdel: Boolean
    ): Call<DeleteProfileImageResponse>
}
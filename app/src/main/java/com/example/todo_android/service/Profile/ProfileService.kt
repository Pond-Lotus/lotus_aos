package com.example.todo_android.service.Profile

import com.example.todo_android.Data.Modify.ChangePassword
import com.example.todo_android.Data.Profile.AuthCode
import com.example.todo_android.Data.Profile.Login
import com.example.todo_android.Data.Profile.Register
import com.example.todo_android.response.ModifyResponse.ChangePasswordResponse
import com.example.todo_android.response.ModifyResponse.ChangeProfileResponse
import com.example.todo_android.response.ModifyResponse.DeleteProfileImageResponse
import com.example.todo_android.response.ProfileResponse.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {

    @POST("/account/login/")
    fun requestLogin(
        @Body loginRequest: Login
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
        @Body authCodeRequest: AuthCode
    ) : Call<AuthCodeResponse>

    @POST("/account/register/")
    fun requestRegister(
        @Body registerRequest: Register
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
        @Body changePasswordRequest: ChangePassword
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
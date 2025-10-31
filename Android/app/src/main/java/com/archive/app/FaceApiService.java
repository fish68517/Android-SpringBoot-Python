package com.archive.app;

import com.archive.app.model.CheckPresenceRequest;
import com.archive.app.model.CheckPresenceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 定义所有与后端 Python Flask AI 的API接口
 */
public interface FaceApiService {

    @POST("/check_presence")
    Call<CheckPresenceResponse> checkPresence(@Body CheckPresenceRequest request);

    // 你也可以把 /register 和 /login 的接口也定义在这里
}
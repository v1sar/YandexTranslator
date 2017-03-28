package com.v1sar.yandextranslator;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by qwerty on 24.03.17.
 */

public interface ApiService {

    @POST("/api/v1.5/tr.json/translate")
    Call<Answer> getMyJSON(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);
}
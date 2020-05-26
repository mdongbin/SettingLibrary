package com.example.settinglibrary.libRetrofit2;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Retrofit2Interface {
    @GET("/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json")
    Call<Map<String,Object>> getBoxOffice(@Query("key") String key,
                                          @Query("targetDt") String targetDt);
}

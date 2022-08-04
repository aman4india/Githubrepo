package com.aman.githubrepo;

import com.aman.githubrepo.models.GitResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServices {

    @GET("/search/repositories")
    Call<GitResponse> getRepositoryList(@QueryMap(encoded = false) Map<String,String> filter );


}

package com.example.stud.musicapp.api;

/**
 * Created by W57020 on 2018-04-19.
 */

public class ApiService {

    public static ApiClinet get_Service(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theaudiodb.com/api/v1/json/{APIKEY}/")
                .build();

        return retrofit.create(ApiClient.class);


    }



}

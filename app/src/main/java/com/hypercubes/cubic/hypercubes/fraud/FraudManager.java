package com.hypercubes.cubic.hypercubes.fraud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FraudManager {

    // Member variables
    OkHttpClient mOkHttpClient;
    private GsonBuilder mGsonBuilder = null;
    private Gson mGson = null;

    private ArrayList<FraudInstance> fraudInstances = new ArrayList<>();

    public FraudManager(){
        mOkHttpClient = new OkHttpClient();
        mGsonBuilder = new GsonBuilder();
        mGson = mGsonBuilder.create();
    }

    public void initializeFraudManager() throws IOException {
        //TODO make some call to server
//        Request request = new Request.Builder()
//                .url("url")
//                .build();
//
//        Response response = mOkHttpClient.newCall(request).execute();
//        String fraudListJson = response.body().string();
//
//        // Massage the information with GSON
//        fraudInstances = mGson.fromJson(fraudListJson, new TypeToken<ArrayList<FraudInstance>>(){}.getType());
    }

    public ArrayList<FraudInstance> getFraudInstances(){
        return fraudInstances;
    }


}

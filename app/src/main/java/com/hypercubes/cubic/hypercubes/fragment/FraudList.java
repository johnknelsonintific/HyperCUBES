package com.hypercubes.cubic.hypercubes.fragment;

import com.google.gson.annotations.SerializedName;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;

import java.util.ArrayList;

public class FraudList {
    @SerializedName("history")
    public ArrayList<FraudInstance> history;

    public FraudList(){

    }
}

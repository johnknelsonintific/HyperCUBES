package com.hypercubes.cubic.hypercubes.fraud;

import java.util.Date;

/**
 * An instance of detected fraud
 */
public class FraudInstance {
    Date timestamp;
    String imageBase64;
    float lat;
    float lng;
    String area;// Area of town
    Integer busId;// Bus that this happened on

    public FraudInstance(){

    }
}

package com.iprocessor.constants;

public enum  SubscriptionType {

    ONEMONTH(3),
    THREEMONTH(5),
    ONEYEAR(10);

    int cost;
    SubscriptionType(int cost){
        this.cost=cost;
    }


}

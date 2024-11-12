package com.example.BugByte_backend.Adapters;

import java.util.Map;

public interface IAdapter <T>{

    //convert object to Map
    Map<String, Object> toMap(T object);

    //convert Map to object
    T fromMap(Map<String, Object> map);

    //convert object to json
    String toJson(T object);
}

package com.meribindiyaemployee.meribindiyaemployee.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MyJsonDeserializer<T>  implements JsonDeserializer<T> {

    @Override
    public  T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement content = json.getAsJsonObject().get("object");
        return new Gson().fromJson(content,typeOfT);

    }

}

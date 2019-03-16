package com.component.fx.plugin_base.network.converter;

import com.component.fx.plugin_base.network.exception.NewsResultException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class NewsGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Gson gson;
    private Type type;

    public NewsGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonElement jsonElement = new JsonParser().parse(value.string());
        if (getErrorCode(jsonElement) == 0) {//成功
            return getResult(jsonElement);
        } else {//失败
            throw new NewsResultException(getReason(jsonElement), getErrorCode(jsonElement), getResultCode(jsonElement));
        }
    }

    private int getErrorCode(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("error_code").getAsInt();
    }

    private T getResult(JsonElement jsonElement) {
        return gson.fromJson(jsonElement.getAsJsonObject().get("result"), type);
    }

    private String getReason(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("reason").getAsString();
    }

    private String getResultCode(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get("resultcode").getAsString();
    }
}

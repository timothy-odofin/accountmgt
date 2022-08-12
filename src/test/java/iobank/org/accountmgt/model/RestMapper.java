package iobank.org.accountmgt.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

public class RestMapper {
    private static Gson getGson(){
        return new Gson();
    }
    public static String mapToJson(Object obj) throws JsonProcessingException {

        return getGson().toJson(obj);
    }
    public static <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {


        return getGson().fromJson(json, clazz);
    }
}

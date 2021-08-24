package com.example.skeleton.constant;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class RestfulApiVersion {

    public static final String V1 = "/api/v1";

    public static final String V1_USER = V1 + "/user";
    public static final String V1_ROLE = V1 + "/role";
    public static final String V1_OPERATION_LOG = V1 + "/operation-log";
    public static final String V1_PERMISSION = V1 + "/permission";

    public static final String V1_SAMPLE = V1 + "/sample";

    public static final String SYSTEM = "/system";

    //对于白名单中的URL，不检查JWT和鉴权
    public static final List<Pair<HttpMethod, String>> ANT_WHITE_LIST = new ArrayList<>();
    public static final List<Pair<HttpMethod, String>> REG_WHITE_LIST = new ArrayList<>();

    static {
        ANT_WHITE_LIST.add(Pair.of(HttpMethod.GET, "/favicon.ico"));
        ANT_WHITE_LIST.add(Pair.of(HttpMethod.POST, V1_USER + "/login"));
        ANT_WHITE_LIST.add(Pair.of(HttpMethod.GET, SYSTEM + "/sms/send"));
        REG_WHITE_LIST.add(Pair.of(HttpMethod.GET, "/swagger.*"));
        REG_WHITE_LIST.add(Pair.of(HttpMethod.GET, "/v3/.*"));
    }

}



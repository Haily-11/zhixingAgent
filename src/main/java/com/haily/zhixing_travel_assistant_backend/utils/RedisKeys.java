package com.haily.zhixing_travel_assistant_backend.utils;

public class RedisKeys {
    public final static String PREFIX = "hailycode:zhixing:";


    public final static String USER_AUTH_TOKEN_PREFIX = PREFIX + "user:auth:";

    //7天
    public final static Integer USER_AUTH_TOKEN_TIMEOUT = 60 * 60 * 24 * 7;

    public final static String RAG_CACHE_PREFIX = PREFIX + "rag:cache:";


    public static String getUserAuthToken(String uid) {
        return USER_AUTH_TOKEN_PREFIX + uid;
    }

    public static String getRagCacheKey(String digest) {
        return RAG_CACHE_PREFIX + digest;
    }

}

//按业务模块再细分，形成 项目:模块:功能 的三级结构
//最终 key 类似：shanyangcode:zhixing:user:auth:12345
//一眼就能看出这个 key 属于哪个业务，方便排查问题
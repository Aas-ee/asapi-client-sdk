package com.aasee.asapiclientsdk.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.aasee.asapiclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.aasee.asapiclientsdk.utils.SignUtils.getSign;


/**
 * 调用第三方接口的客户端
 *
 * @author Aasee
 */
@Slf4j
public class AaseeApiClient {

    private String accessKey;

    private String secretKey;

    private static final String GATEWAY_HOST = "http://localhost:8090";

    public AaseeApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/get", paramMap);
        log.info("result: " + result);
        return result;
    }

    public String getNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/name/post", paramMap);
        log.info("result: " + result);
        return result;
    }

    private Map<String, String > getHeaderMap(String body) throws UnsupportedEncodingException {
        Map<String, String > headerMap = new HashMap<>();
        headerMap.put("accessKey", accessKey);
        // 一定不能直接发送给后端
//        headerMap.put("secretKey", secretKey);
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        headerMap.put("body", URLEncoder.encode(body,"utf-8"));
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("sign", getSign(body, secretKey));
        return headerMap;
    }


    public String getUserNameByPost(User user) throws UnsupportedEncodingException {
        String json = JSONUtil.toJsonStr(user);
//        String encode = URLEncoder.encode(json, "utf-8");
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .headerMap(getHeaderMap(json),false)
                .body(json)
                .execute();
//        HttpUtil.post("http://localhost:8123/api/name/user",)
        log.info("status: "+httpResponse.getStatus());
        String result = httpResponse.body();
        log.info("result: " + result);
        return result;
    }
}

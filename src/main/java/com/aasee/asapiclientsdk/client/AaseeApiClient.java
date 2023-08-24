package com.aasee.asapiclientsdk.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.aasee.asapiclientsdk.model.User;
import com.aasee.asapicommon.model.entity.SmartBoxKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

//    private static final String GATEWAY_HOST = "http://localhost:8090";
//    private static final String GATEWAY_HOST = "http://121.37.221.78:8090";
    private static final String GATEWAY_HOST = "http://43.136.41.32:8090";

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


    public String getSmartBoxMusicByPost(SmartBoxKey smartBoxKey) throws UnsupportedEncodingException {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("key", key);
//        String result = HttpUtil.post("http://121.37.221.78:5000/api/getKWMulist", paramMap);
//        log.info("result: " + result);
        String json = JSONUtil.toJsonStr(smartBoxKey);
        //        String encode = URLEncoder.encode(json, "utf-8");
        log.info("json: " + json);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/smartBox")
//                .header(Header.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8")
                .headerMap(getHeaderMap(json),false)
                .form("key",smartBoxKey.getKey())
                .execute();
//        HttpUtil.post("http://localhost:8123/api/name/user",)
        log.info("status: "+httpResponse.getStatus());
        String result = httpResponse.body();
//        String decode = URLDecoder.decode(result, "utf-8");
//        JSON parse = JSONUtil.parse(result);
        log.info("result: " + result);
        return result;
    }


    public String getRecognitionByPost(MultipartFile recognition) throws IOException {
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("key", key);
//        String result = HttpUtil.post("http://121.37.221.78:5000/api/getKWMulist", paramMap);
//        log.info("result: " + result);
        String json = JSONUtil.toJsonStr(recognition.getName());
        //        String encode = URLEncoder.encode(json, "utf-8");
        log.info("json: " + json);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/recognition")
//                .header(Header.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8")
                .headerMap(getHeaderMap(json),false)
                .form("userfile", recognition.getBytes(), "flash_code.jpg")
                .execute();
//        HttpUtil.post("http://localhost:8123/api/name/user",)
        log.info("status: "+httpResponse.getStatus());
        String result = httpResponse.body();
//        String decode = URLDecoder.decode(result, "utf-8");
//        JSON parse = JSONUtil.parse(result);
        log.info("result: " + result);
        return result;
    }


    public String getAncientPoetryByGet() throws IOException {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/ancientPoetry")
                .header("X-User-Token","nxR2ZQzjGOuYSGrVc04LAc4kT8SHcoui")
                .headerMap(getHeaderMap("AncientPoetry"),false)
                .execute();
        log.info("status: "+httpResponse.getStatus());
        String result = httpResponse.body();
//        InputStream inputStream = httpResponse.bodyStream();
//        // 将InputStream中的数据读取到内存中
//        ByteArrayOutputStream result = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) != -1) {
//            result.write(buffer, 0, length);
//        }
//
//// 将读取的数据转换为String对象
//        String str = result.toString("UTF-8");
//        log.info("result: " + str);
        log.info("result: " + result);
        return result;
    }



}

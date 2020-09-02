package io.starskyoio.asr.baidu.aip.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import io.starskyoio.asr.baidu.aip.exception.BaiduAIPException;
import io.starskyoio.asr.baidu.aip.model.AuthResponse;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 用于获取百度开发平台token
 *
 * @author Linus Lee
 * @version 1.0
 */
public class AuthClient {
    private static final int FAIL = -1;
    private static final String TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";
    private String tokenUrl;
    private String apiKey;
    private String secretKey;
    private ParserConfig parserConfig;

    public AuthClient(String tokenUrl, String apiKey, String secretKey) {
        if(StringUtils.isEmpty(tokenUrl)){
            this.tokenUrl = TOKEN_URL;
        }
        this.tokenUrl = tokenUrl;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.tokenUrl += "?grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + secretKey;
        this.parserConfig = new ParserConfig();
        this.parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    public AuthResponse getToken() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(this.tokenUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                AuthResponse result = JSON.parseObject(response.body().string(), AuthResponse.class, this.parserConfig);
                if (result.getError() != null) {
                    throw new BaiduAIPException(FAIL, result.getErrorDescription());
                }
                return result;
            }
            throw new BaiduAIPException("获取token失败");
        } catch (IOException e) {
            throw new BaiduAIPException("获取token失败", e);
        }
    }
}

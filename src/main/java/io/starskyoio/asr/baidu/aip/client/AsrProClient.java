package io.starskyoio.asr.baidu.aip.client;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import io.starskyoio.asr.baidu.aip.exception.BaiduAIPException;
import io.starskyoio.asr.baidu.aip.model.AsrProResponse;
import okhttp3.*;

import java.io.IOException;

/**
 * 百度语音识别极速版客户端
 *
 * @author Linus Lee
 * @version 1.0
 */
public class AsrProClient {
    private static final String API_URL = "https://vop.baidu.com/pro_api";
    private String apiUrl;
    private ParserConfig parserConfig;
    private OkHttpClient client;

    public AsrProClient(String apiUrl) {
        if(StringUtils.isEmpty(apiUrl)){
            this.apiUrl = API_URL;
        }
        this.apiUrl = apiUrl;
        this.parserConfig = new ParserConfig();
        this.parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        this.client = new OkHttpClient();
    }

    public AsrProResponse exec(byte[] data, String token) {
        Request request = new Request.Builder().url(this.apiUrl + "?dev_pid=80001&cuid=asr_pro_client&token=" + token)
                .header("Content-Type", "audio/pcm;rate=16000")
                .post(RequestBody.create(data))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return JSON.parseObject(response.body().string(), AsrProResponse.class, this.parserConfig);
            }
            throw new BaiduAIPException("调用语音识别接口失败");
        } catch (IOException e) {
            throw new BaiduAIPException("调用语音识别接口失败", e);
        }
    }

}

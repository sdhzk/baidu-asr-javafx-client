package io.starskyoio.asr.baidu.aip.client;

import com.alibaba.fastjson.JSON;
import io.starskyoio.asr.baidu.aip.model.AsrProResponse;
import io.starskyoio.asr.baidu.aip.model.AuthResponse;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ArsProClientTest {
    private static final String API_KEY = "TxzTadwR0v0mmgGbxmsB7zIA";
    private static final String SECRET_KEY = "corx1AFw1YE18TzLDl3G3Fqh2AzK0sgT";

    @Test
    public void testAuthClient() {
        AuthClient authClient = new AuthClient(null, API_KEY, SECRET_KEY);
        AuthResponse response = authClient.getToken();
        Assert.assertNotNull(response);
        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testArsProClient() throws IOException {
        byte[] data = FileUtils.readFileToByteArray(new File("D:\\201805300859314375_93.525mhz_320khz_250khz.wav"));
        AuthClient authClient = new AuthClient(null, API_KEY, SECRET_KEY);
        AuthResponse authResponse = authClient.getToken();
        AsrProClient asrProClient = new AsrProClient(null);
        AsrProResponse response = asrProClient.exec(data, authResponse.getAccessToken());
        Assert.assertNotNull(response);
        System.out.println(JSON.toJSONString(response));
    }
}
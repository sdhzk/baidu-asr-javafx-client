package io.starskyoio.asr.service;

import com.alibaba.fastjson.JSON;
import io.starskyoio.asr.baidu.aip.client.AsrProClient;
import io.starskyoio.asr.baidu.aip.model.AsrProResponse;
import io.starskyoio.asr.controller.MainContorller;
import io.starskyoio.asr.holder.TokenHolder;
import io.starskyoio.asr.model.TaskResultModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class VopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainContorller.class);
    private static final int SUCCESS = 0;
    @Autowired
    private AsrProClient asrProClient;

    @Async("asyncServiceExecutor")
    public void doTask(File file, Callback callback) {
        TaskResultModel result = new TaskResultModel();
        result.setFileName(file.getName());
        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            AsrProResponse response = asrProClient.exec(data, TokenHolder.token);
            if(response.getErrNo() == SUCCESS){
                result.setContent(response.getResult()[0]);
                result.setErrNo(0);
                result.setErrMsg("成功");
            }else{
                result.setContent("");
                result.setErrNo(response.getErrNo());
                result.setErrMsg(response.getErrMsg());
            }
            LOGGER.info("调用语音识别接口成功, path:{}, response:{}", file.getAbsolutePath(), JSON.toJSONString(response));
        } catch (Exception e) {
            result.setContent("");
            result.setErrNo(-1);
            result.setErrMsg(e.getMessage());
            LOGGER.error("调用语音识别接口失败，errorMsg:{}", e.getMessage());
        }
        callback.call(result);
    }

    public interface Callback {
        void call(TaskResultModel result);
    }
}

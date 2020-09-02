package io.starskyoio.asr.service;

import io.starskyoio.asr.model.OutputExcelModel;
import io.starskyoio.asr.util.AlertUtils;
import io.starskyoio.asr.util.EasyExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

    @Async("asyncServiceExecutor")
    public void doTask(String outFile, List<OutputExcelModel> data) {
        try {
            EasyExcelUtils.write(outFile, data, OutputExcelModel.class);
            LOGGER.info("生成excel成功, outFile:{}", outFile);
        }catch (Exception e){
            LOGGER.error("生成excel失败, errorMsg:{}", e.getMessage());
            AlertUtils.error("导出失败");
        }
    }
}

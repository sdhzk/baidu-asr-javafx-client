package io.starskyoio.asr.util;

import io.starskyoio.asr.model.OutputExcelModel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelUtilsTest {
    @Test
    public void testWrite() {
        String outFile = "D:\\result.xlsx";
        List<OutputExcelModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OutputExcelModel model = new OutputExcelModel();
            model.setProofNo(i+"");
            model.setSignalNo(i+"");
            model.setContent("测试"+i);
            model.setResult("1");
            model.setFail("成功");
            list.add(model);
        }
        EasyExcelUtils.write(outFile, list, OutputExcelModel.class);
    }

}

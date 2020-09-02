package io.starskyoio.asr.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class OutputExcelModel  extends BaseRowModel {
    @ExcelProperty(value="证据编号", index = 0)
    private String proofNo;

    @ExcelProperty(value="信号编号", index = 1)
    private String signalNo;

    @ExcelProperty(value="解析结果（1：成功；2：失败）", index = 3)
    private String result;

    @ExcelProperty(value="语音内容", index = 4)
    private String content;

    @ExcelProperty(value="失败原因", index = 5)
    private String fail;
}

package io.starskyoio.asr.model;

import lombok.Data;

@Data
public class TaskResultModel {
    private String fileName;
    private String content;
    private Integer errNo;
    private String errMsg;
}

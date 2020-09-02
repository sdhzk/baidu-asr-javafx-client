package io.starskyoio.asr.baidu.aip.model;

import lombok.Data;

@Data
public class AsrProResponse {
    private Integer errNo;
    private String errMsg;
    private String corpusNo;
    private String sn;
    private String[] result;
}

package io.starskyoio.asr.baidu.aip.exception;

public class BaiduAIPException extends RuntimeException {
    private Integer errCode;
    private String errMsg;

    public BaiduAIPException(String message) {
        super(message);
    }

    public BaiduAIPException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaiduAIPException(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BaiduAIPException(Integer errCode, String errMsg, Throwable cause) {
        super(cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

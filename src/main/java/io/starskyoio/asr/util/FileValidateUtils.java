package io.starskyoio.asr.util;

import java.util.regex.Pattern;

/**
 * 语音文件校验工具类
 * @author Linus Lee
 * @version 1.0
 */
public class FileValidateUtils {
    private static final Pattern pattern = Pattern.compile("^\\S+_\\S+_\\S+\\.wav$");

    public static boolean checkFileName(String fileName) {
        return pattern.matcher(fileName).matches();
    }
}

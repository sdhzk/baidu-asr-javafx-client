package io.starskyoio.asr.util;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FileValidateUtilsTest {
    @Test
    public void testCheckFileName() {
        String fileName = "201805300859314375_93.525mhz_1000.wav";
        boolean flag = FileValidateUtils.checkFileName(fileName);
        Assert.assertTrue(flag);
    }

    @Test
    public void testGetBaseFileName() {
        String fileName = "201805300859314375_93.525mhz_1000.wav";
        String dirName = "C:\\abc\\10001";
        File file = new File(dirName);
        System.out.println(FilenameUtils.getBaseName(fileName));
        System.out.println(file.getName());
    }
}

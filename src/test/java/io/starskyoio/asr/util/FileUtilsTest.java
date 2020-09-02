package io.starskyoio.asr.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtilsTest {
    @Test
    public void listFiles() {
        List<File> files = FileUtils.listFiles(new File("C:\\Users\\Administrator\\Desktop\\20190102"), null, true)
                .stream()
                .filter(file -> FileValidateUtils.checkFileName(file.getName()))
                .collect(Collectors.toList());
        files.stream().forEach(System.out::println);
    }
}

package com.ask.sample.util;

import com.ask.sample.advice.exception.InvalidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    public static void upload(MultipartFile multipartFile, String uploadDir, String savedFileDir) {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new InvalidationException(String.format("make directory(%s) fail", uploadDir));
            }
        }

        try {
            multipartFile.transferTo(new File(savedFileDir));
            log.debug("upload finished...({}) ", savedFileDir);
        } catch (IOException ex) {
            throw new InvalidationException(String.format("file upload error:%s", savedFileDir));
        }
    }
}
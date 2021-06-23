package com.ask.sample.util;

import com.ask.sample.advice.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Slf4j
public final class FileUtils extends org.apache.commons.io.FileUtils {

    public static void upload(MultipartFile multipartFile, String uploadDir, String savedFileDir) {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new BusinessException(String.format("make directory(%s) fail", uploadDir));
            }
        }

        try {
            multipartFile.transferTo(new File(savedFileDir));
            log.debug("upload finished...({}) ", savedFileDir);
        } catch (IOException ex) {
            throw new BusinessException(String.format("file upload error:%s", savedFileDir));
        }
    }

    public static void downloadFile(HttpServletResponse response, String savedFileDir, String fileNm) {
        File file = new File(savedFileDir);

        if (!file.exists()) {
            throw new BusinessException("file not found");
        }

        try (FileInputStream fi = new FileInputStream(file)) {
            byte[] buf = new byte[1024*10];
            int len = 0;
            fileNm = StringUtils.getDownloadFilename(fileNm);
            response.setContentType("application/octet-stream");
            response.setContentLength((int)file.length());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition","attachment; filename=\"" + fileNm + "\"");

            OutputStream fo = response.getOutputStream();
            while ( (len = fi.read(buf)) > 0) {
                fo.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            throw new BusinessException("file not found : " + file.getName());
        } catch (IOException e) {
            throw new BusinessException("IOException : " + e.getMessage());
        }
    }
}
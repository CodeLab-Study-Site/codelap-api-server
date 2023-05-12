package com.codelap.integration.s3;

import com.codelap.common.support.FileStandard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Profile({"local", "default"})
@Component
@Slf4j
public class LocalUploader implements FileUpload {
    @Override
    public FileStandard upload(MultipartFile multipartFile, String dirName, FileStandard file) throws IOException {
        return file.create("savedName", "originalName", 1L);
    }

    @Override
    public List<FileStandard> uploads(List<MultipartFile> multipartFiles, String dirName, FileStandard file) throws IOException {
        return null;
    }
}

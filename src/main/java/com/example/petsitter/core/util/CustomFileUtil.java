package com.example.petsitter.core.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${org.example.upload.path}")
    private String uploadPath;

    @PostConstruct // 생성자 대신 초기화 작업시 사용 = 폴더 만들어주기
    public void init(){
        File tempFolder = new File(uploadPath);
        if(!tempFolder.exists()){
            tempFolder.mkdir();
            //폴더 만들어주기
        }
        uploadPath = tempFolder.getAbsolutePath();
    }

    //파일 업로드
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
        // 파일이 없을때
        if(files == null || files.size() == 0){
            return List.of();
        }

        //실제로 업로드 된 파일의 이름을 저장
        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file : files){
            if (file.isEmpty()) {
                continue; // 비어있는 파일은 무시
            }

            //UUID 32자리 + 원래 파일 이름 file.getOriginalFilename
            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, saveName);

            try {
                // 파일저장 - 예외 처리
                Files.copy(file.getInputStream(), savePath); //원본파일 업로드
                
                // 이미지인 경우에만 썸네일 만들기
                String contentType = file.getContentType(); // Mine Type
                if(contentType != null || contentType.startsWith("image")){
                    // 썸네일 이미지
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

                    Thumbnails.of(savePath.toFile()).size(200,200).toFile(thumbnailPath.toFile());
                }

                uploadNames.add(saveName);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } // end for

        return uploadNames;

    }

    public List<String> saveSlideFiles(List<MultipartFile> files) throws RuntimeException{
        // 파일이 없을때
        if(files == null || files.size() == 0){
            return List.of();
        }

        //실제로 업로드 된 파일의 이름을 저장
        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile file : files){
            if (file.isEmpty()) {
                continue; // 비어있는 파일은 무시
            }

            //UUID 32자리 + 원래 파일 이름 file.getOriginalFilename
            String saveName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, saveName);

            try {
                // 파일저장 - 예외 처리
                Files.copy(file.getInputStream(), savePath); //원본파일 업로드

                // 이미지인 경우에만 썸네일 만들기
                String contentType = file.getContentType(); // Mine Type
                if(contentType != null || contentType.startsWith("image")){
                    // 썸네일 이미지
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

                    Thumbnails.of(savePath.toFile()).size(600,600).toFile(thumbnailPath.toFile());
                }

                uploadNames.add(saveName);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } // end for

        return uploadNames;

    }

    public ResponseEntity<Resource> getFile(String fileName) {

        Resource resource = new FileSystemResource(uploadPath+ File.separator + fileName);

        if(!resource.exists()) {

            resource = new FileSystemResource(uploadPath+ File.separator + "default.jpg");

        }

        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public void deleteFiles(List<String> fileNames) {
        if(fileNames == null || fileNames.size() == 0){
            return;
        }

        fileNames.forEach(fileName -> {

            //썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }
}

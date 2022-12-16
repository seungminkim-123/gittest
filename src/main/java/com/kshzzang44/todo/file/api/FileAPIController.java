package com.kshzzang44.todo.file.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileAPIController {
    @Value("${file.image.todo}") String todo_img_path;
    @Value("${file.image.member}") String member_img_path;

    @PutMapping("/{type}/upload")
    public ResponseEntity<Object> putImageUpload(
        // 파일 객체 변수 - file (전송 할 떄도 파일의 파라미터 이름을 file로 지정해야한다.)
        @PathVariable String type, @RequestPart MultipartFile file,@RequestParam Long seq
    ){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        //업로드할 파일의 원본이름 확장자까지
        System.out.println(file.getOriginalFilename());
        //Path-폴더 및 파일의 위치를 나타내는 객체
        //Paths - 폴더 및 파일을 가져오고 경로를 만들기 위한 파일 유틸리티 클래스
        // todo_img_path 문자열로부터 실제 폴더 경로를 가져온다
        Path folderLocation = null;
        if (type.equals("todo")){
            folderLocation = Paths.get(todo_img_path);
        }
        else if(type.equals("member")){
            folderLocation = Paths.get(member_img_path);
        }
        else{
            map.put("status", false);
            map.put("message","타입정보가 잘못됌.");
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }

        String originFileName = file.getOriginalFilename();
        String[] split = originFileName.split("\\.");
        String ext = split[split.length - 1];
        String filename = "";
        for(int i = 0; i < split.length - 1;i++){
            filename += split[i];
        }
        String saveFilename = type +"";
        Calendar c =Calendar.getInstance();
        saveFilename = c.getTimeInMillis()+"."+ext;

        // 폴더 경로와 파일의 이름을 합쳐서 목표파일의 경로로 만든다
        Path targetFile = folderLocation.resolve(saveFilename);
        try {
            //files는 파일 처리에 대한 유틸리티 클래스
            // copy - 복사 file.getInpuStream()-파일을 열어서 내용을 읽는 준비
            // targetFile경로로, standardCopyOption.REPLACE_EXISTING - 덮어쓰기 모드
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(
    @PathVariable String filename, HttpServletRequest request
    ){
        //todo_img_path
        Path folderLocation = Paths.get(todo_img_path);
        //폴더경로롸 파일의 이름을 합쳐서 목표 파일의 경로를만든다
        Path targetFile = folderLocation.resolve(filename);
        //다운로드 가능한 형태로 만들이기위한 resource객체 생성
        Resource r = null;
        try{
            //일반파일->url로 첨부 가능한 형태로 변환
            r = new UrlResource(targetFile.toUri());
        }
        catch(Exception e){ e.printStackTrace();}
        //첨부된 파일의 타입을 저장하기위한 변수 생성
        String contentType = null;
        try {
            // 첨부할 파일의 파일 정보 산출
            contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            // 산출한 파일의 타입이 null이라면
            if (contentType == null){
                //일반 파일로 처리한다
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()//응답의 코드를 200ok로 설정하고
            .contentType(MediaType.parseMediaType(contentType))//산출한 타입을 응답에 맞는 형태로 변환
            //내보낼 내용의 타입을 설정(파일),
            //"attachment; filename*=\""+r.getFilename()+"\" 요청한 쪽에서 다운로드 한 파일의 이름을 결정
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename*=\""+r.getFilename()+"\"")
            .body(r);//변환된 파일을 ResponseEntity에 추가
    }


}


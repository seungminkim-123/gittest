package com.kshzzang44.todo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kshzzang44.todo.entity.MemberInfoEntity;
import com.kshzzang44.todo.entity.TodoImageEntity;
import com.kshzzang44.todo.entity.TodoInfoEntity;
import com.kshzzang44.todo.repository.TodoImageRepository;
import com.kshzzang44.todo.repository.TodoInfoRepository;
@Service
public class TodoInfoService {
    @Autowired TodoInfoRepository t_repo;
    @Autowired TodoImageRepository ti_repo;
    public Map<String, Object> addTodoInfo(TodoInfoEntity data, HttpSession session){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if (loginUser == null){
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요합니다");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }
        else{
            data.setMiSeq(loginUser.getSeq());
            t_repo.save(data);
            resultMap.put("status", true);
            resultMap.put("message", "일정이 추가되었습니다");
            resultMap.put("code", HttpStatus.CREATED);
        }

        return resultMap;
    }

    public Map<String, Object> getTodoList(HttpSession session){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if (loginUser == null){
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요합니다");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }
        else{
            resultMap.put("status", true);
            resultMap.put("message", "리스트가 있습니다");
            resultMap.put("list",t_repo.findAllByMiSeq(loginUser.getSeq()));
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    public Map<String, Object> updateTodoStatus(Integer status, Long seq){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        TodoInfoEntity todo =t_repo.findBySeq(seq);
        if (todo == null){
            resultMap.put("status", false);
            resultMap.put("message", "잘못된 Todo 번호입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }
        else{
            todo.setStatus(status);
            t_repo.save(todo);
            resultMap.put("status", true);
            resultMap.put("message", "Todo 상태가 변경되었습니다");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    public Map<String, Object> updateTodoStatus(String content, Long seq){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        TodoInfoEntity todo =t_repo.findBySeq(seq);
        if (todo == null){
            resultMap.put("status", false);
            resultMap.put("message", "잘못된 Todo 번호입니다.");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }
        else{
            todo.setContent(content);
            t_repo.save(todo);
            resultMap.put("status", true);
            resultMap.put("message", "Todo 상태가 변경되었습니다");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    @Transactional
    public Map<String, Object> deleteTodoStatus(Long seq, HttpSession session){ //필요한건 todo_list의 번호와 로그인되어있는 세션정보
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>(); //반환해줄 map 선언
        MemberInfoEntity loginUser = (MemberInfoEntity) session.getAttribute("loginUser"); // 쎄션 불러오기
        if (loginUser == null){ // 불러온 로그인이 정보가없다면 로그인이 필요하다고 return 해주기
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요합니다");
            resultMap.put("code", HttpStatus.FORBIDDEN);
            return resultMap;
        }
        TodoInfoEntity todo = t_repo.findBySeqAndMiSeq(seq, loginUser.getSeq()); // 로그인 정보가 있기떄문에 받은 seq과 유저의 seq를 이용해서 작성한 게시글을 찾음
        if (todo == null){// 찾은 값이 없다면 없다고함
            resultMap.put("status",false);
            resultMap.put("message", "잘못된 todo번호이거나, 권한이없습니다");
            resultMap.put("code", HttpStatus.FORBIDDEN);
        }
        else{// 찾았다면 
            t_repo.deleteBySeqAndMiSeq(seq, loginUser.getSeq());
            resultMap.put("status",false);
            resultMap.put("message", "todo가 삭제되었습니다");
            resultMap.put("code", HttpStatus.OK);
        }
        return resultMap;
    }

    public Map<String, Object> selectTodeListByList(
        HttpSession session, String start, String end
    ){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity) session.getAttribute("loginUser");
        if (loginUser == null){
            resultMap.put("status", false);
            resultMap.put("message", "로그인이 필요합니다");
            resultMap.put("code", HttpStatus.FORBIDDEN);
            return resultMap;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date startDt = null;
        Date endDt = null;
        try{
            startDt = formatter.parse(start);
            endDt = formatter.parse(end);
        }
        catch(Exception e){
            resultMap.put("status",false);
            resultMap.put("message", "날짜 형식을 확인해주세요(yyyyMMdd ex:20221214)");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        resultMap.put("status",true);
        resultMap.put("message", "조회완료");
        resultMap.put("list", t_repo.findByEndDtBetweenAndMiSeq(startDt, endDt, loginUser.getSeq()));
        resultMap.put("code", HttpStatus.OK);
        return resultMap;
    }

    public Map<String, Object> addTodoImage(TodoImageEntity data, Long tiSeq){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        data.setTiSeq(tiSeq);
        ti_repo.save(data);
        resultMap.put("status", true);
        resultMap.put("message", "이미지가 저장됌");
        resultMap.put("code", HttpStatus.OK);
        return resultMap;
    }
}

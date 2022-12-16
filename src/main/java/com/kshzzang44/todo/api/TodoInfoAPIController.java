package com.kshzzang44.todo.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kshzzang44.todo.entity.TodoInfoEntity;
import com.kshzzang44.todo.service.TodoInfoService;

// ResponseEntity 클래스는 HTTP 상태 코드, HTTP 헤더, 바디 내용을 제공하는 응답을 만들 수 있는 기능을 제공합니다




@RestController
@RequestMapping("/api/todo")
public class TodoInfoAPIController {
    @Autowired TodoInfoService tService;

    @PutMapping("/add")
    public ResponseEntity<Object> insertTodo(
        @RequestBody TodoInfoEntity data, HttpSession session
    ){
        Map<String, Object> resultMap = tService.addTodoInfo(data, session);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getTodoList(HttpSession session){
        Map<String, Object> resultMap = tService.getTodoList(session);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    @PatchMapping("/update/{type}")
    public ResponseEntity<Object> updateTodoStatus(
        HttpSession session, @PathVariable String type, @RequestParam String value, @RequestParam Long seq
    ){
        if (type.equals("status")){
            Map<String, Object> map = tService.updateTodoStatus(Integer.parseInt(value), seq);
            return new ResponseEntity<>(map,(HttpStatus)map.get("code"));
        }
        else if (type.equals("content")){
            Map<String, Object> map = tService.updateTodoStatus(value, seq);
            return new ResponseEntity<>(map,(HttpStatus)map.get("code"));
        }
        else{
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("status", false);
            map.put("message", "type은 status , conet 둘중한가지만");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTode(HttpSession session, @RequestParam Long seq){
        Map<String, Object> resultMap = tService.deleteTodoStatus(seq, session);
        return new ResponseEntity<>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    @GetMapping("/list/term")
    public ResponseEntity<Object> deleteTode(
        HttpSession session, @RequestParam String start, @RequestParam String end
    ){
        Map<String, Object> resultMap = tService.selectTodeListByList(session,start,end);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }
    

}

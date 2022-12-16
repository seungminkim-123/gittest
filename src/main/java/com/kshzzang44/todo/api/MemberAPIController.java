package com.kshzzang44.todo.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kshzzang44.todo.data.LoginVO;
import com.kshzzang44.todo.entity.MemberInfoEntity;
import com.kshzzang44.todo.repository.memberRepository;
import com.kshzzang44.todo.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberAPIController {
    @Autowired MemberService mService;
    @PutMapping("/join")
    public ResponseEntity<Object> memberJoin(@RequestBody MemberInfoEntity data){
        Map<String, Object> resultMap = mService.addMember(data);
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }

    @PostMapping("/login")
    public ResponseEntity<Object>  memberLogin(@RequestBody LoginVO data, HttpSession session){
        Map<String, Object> resultMap = mService.loginMember(data);
        session.setAttribute("loginUser", resultMap.get("loginUser"));
        return new ResponseEntity<Object>(resultMap, (HttpStatus)resultMap.get("code"));
    }


    
    // @Autowired memberRepository repo;
    // @PostMapping("/logintest")
    // public ResponseEntity<Object>  memberLogintest(@RequestBody LoginVO data){
    //     Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
    //     if (repo.findByEmailAndPwd(data.getEmail(), data.getPwd()) == null){
    //         resultMap.put("message", false);
    //     }
    //     else
    //         resultMap.put("message", true);

    //     return new ResponseEntity<>(resultMap, HttpStatus.OK);
    // }
}

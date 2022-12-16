package com.kshzzang44.todo.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kshzzang44.todo.data.LoginVO;
import com.kshzzang44.todo.entity.MemberInfoEntity;
import com.kshzzang44.todo.repository.memberRepository;
import com.kshzzang44.todo.util.AESAlgorism;

@Service
public class MemberService {
    @Autowired memberRepository m_repo;
    public Map<String, Object> addMember(MemberInfoEntity data){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (m_repo.countByEmail(data.getEmail()) == 1){
            resultMap.put("status", false);
            resultMap.put("message", data.getEmail() + "는 이미 등록된 사용자입니다");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }
        else{
            try{
                String enPwd = AESAlgorism.Encrypt(data.getPwd());
                data.setPwd(enPwd);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            m_repo.save(data);
            resultMap.put("status", true);
            resultMap.put("message", "회원등록되었습니다");
            resultMap.put("code", HttpStatus.CREATED);
        }
        return resultMap;
    }

    public Map<String, Object> loginMember(LoginVO data){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = null;

        try {
            loginUser = m_repo.findByEmailAndPwd(data.getEmail(), AESAlgorism.Encrypt(data.getPwd()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginUser == null){
            resultMap.put("status", false);
            resultMap.put("message", "이메일 또는 비밀번호 오류");
            resultMap.put("code", HttpStatus.NOT_FOUND);
        }
        else{
            resultMap.put("status", true);
            resultMap.put("message", "로그인성공");
            resultMap.put("code",HttpStatus.ACCEPTED);
            resultMap.put("loginUser", loginUser);
        }
        return resultMap;
    }
}

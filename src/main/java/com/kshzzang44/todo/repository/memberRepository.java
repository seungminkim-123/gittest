package com.kshzzang44.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kshzzang44.todo.entity.MemberInfoEntity;

@Repository
public interface memberRepository extends JpaRepository<MemberInfoEntity, Long>{
    public Integer countByEmail(String email);
    public MemberInfoEntity findByEmailAndPwd(String email, String pwd);
}

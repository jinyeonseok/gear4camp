package com.gear4camp.mapper;

import com.gear4camp.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user); // 회원 저장
    User findByUserId(String userId); // 회원 조회
    void updateUser(User user); // 회원 정보 수정
    void deleteUser(String userId); // 회원 삭제
}
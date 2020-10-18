package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.Map;

public interface MemberDao {
    Member isMember(String telephone);

    void addMember(Member member);

    Integer findMemberBayMonths(String regTime);

    Integer todayNewMember(String date);

}

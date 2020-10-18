package com.itheima.demo;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Demo01 {
    @Test
    public void testSpringSecurity(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String s1 = bCryptPasswordEncoder.encode("123456");
        System.out.println(s1);
        //$2a$10$wLnEhrvKHqZqLsnFzl2pZuISKc7ZAmluTIFUsTxEid5wqDLVfV51a
        //String s2 = bCryptPasswordEncoder.encode("123456");
        //System.out.println(s2);
       // boolean b = bCryptPasswordEncoder.matches("天剑", "$2a$10$W9Wu.JXeYPrq3866VAnMsefR5uxDLXIg2h5R0bJgtioDgbVTpaERu");
       // System.out.println(b);
        //$2a$10$OAG5YsmPvRZLcCEcQ6MNmOCeyCHX1vNRHwb8ihXlii0iTXtVUVotC
        //$2a$10$W9Wu.JXeYPrq3866VAnMsefR5uxDLXIg2h5R0bJgtioDgbVTpaERu
    }
}

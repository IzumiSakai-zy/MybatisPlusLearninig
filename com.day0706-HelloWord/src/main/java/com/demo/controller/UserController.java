package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.entities.User;
import com.demo.enums.GenderEnum;
import com.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @RequestMapping("/")
    private String query(){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("gender", GenderEnum.女);
        return userMapper.selectList(wrapper).toString();
    }

    @ResponseBody
    @RequestMapping("/insert")
    private String insert(){
        userMapper.insert(new User());
        return "wanchegn";
    }

    @ResponseBody
    @RequestMapping("/update")
    private String update(){
        User user = userMapper.selectById(8);
        user.setName("Izumi Sakai");
        userMapper.updateById(user);
        return "update";
    }
}

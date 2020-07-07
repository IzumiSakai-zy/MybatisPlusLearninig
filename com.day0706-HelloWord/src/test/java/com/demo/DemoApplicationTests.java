package com.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.entities.User;
import com.demo.entities.UserVo;
import com.demo.enums.GenderEnum;
import com.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }


    @Test
    void query() {
        QueryWrapper wrapper=new QueryWrapper();
        Map<String,Object> map=new HashMap<>();
        map.put("gender",GenderEnum.女);
        map.put("name","Izumi Sakai");
        wrapper.allEq(map);
        System.out.println(userMapper.selectList(wrapper));
    }

    @Test
    void like() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.like("name","zumi");
        System.out.println(userMapper.selectList(wrapper));
    }

    @Test
    void inSql() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.inSql("name","select name from user where name like '%zumi%'");
        List maps = userMapper.selectMaps(wrapper);
        System.out.println(maps );
    }

    @Test
    void page() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.gt("id",0);
        //每三个分一页，查询第一页
        Page<User> page=new Page<>(1,3);
        //查询时传入page和wrapper
        Page resultPage = userMapper.selectPage(page, wrapper);
        //通过getRecords获得结果
        List records = resultPage.getRecords();
        System.out.println(records);
    }

    @Test
    void multiTable(){
        List<UserVo> userVos = userMapper.findAllUserVO(0);
        System.out.println(userVos);
    }

    @Test
    void delet(){
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.like("name","izu");
        //根据查询条件删除
        userMapper.delete(wrapper);
        //根据ID删除
        userMapper.deleteById(3);
        //根据很多ID删除
        userMapper.deleteBatchIds(Arrays.asList(1,2));
    }

    @Test
    void update(){
        User user = userMapper.selectById(8);
        user.setAge(40);
        userMapper.updateById(user);
//        QueryWrapper<User> wrapper=new QueryWrapper<>();
//        wrapper.eq("id",5);
//        userMapper.update(user,wrapper);
    }
}

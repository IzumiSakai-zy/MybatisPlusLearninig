package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entities.User;
import com.demo.entities.UserVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    @Select("select u.*,a.money as account_money from user u,account a where u.id=a.user_id and u.id>#{id}")
    List<UserVo> findAllUserVO(Integer id);
}

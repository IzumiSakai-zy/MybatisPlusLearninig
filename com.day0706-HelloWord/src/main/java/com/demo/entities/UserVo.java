package com.demo.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
    //账户类的余额
    @TableField("account_money")
    private Double accountMoney;

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", accountMoney=" + accountMoney +
                '}';
    }
}

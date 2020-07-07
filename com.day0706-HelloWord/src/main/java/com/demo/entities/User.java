package com.demo.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.demo.enums.GenderEnum;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@TableName(value = "user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name",select = true,fill = FieldFill.DEFAULT )
    private String name;
    private Integer age;
    private String email;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String notExistColumn;
    @Version
    private Integer version;
    @TableField("gender")
    private GenderEnum gender;
    @TableLogic
    private Integer deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNotExistColumn() {
        return notExistColumn;
    }

    public void setNotExistColumn(String notExistColumn) {
        this.notExistColumn = notExistColumn;
    }
}


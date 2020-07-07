package com.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum GenderEnum {
    男(1,"男"),
    女(0,"女");

    GenderEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @EnumValue
    private Integer code;
    private String msg;
}

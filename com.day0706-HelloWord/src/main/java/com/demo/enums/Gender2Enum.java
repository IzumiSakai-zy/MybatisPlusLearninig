package com.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

public enum  Gender2Enum implements IEnum<Integer> {
    男(1,"男"),
    女(0,"女");

    Gender2Enum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @EnumValue
    private Integer code;
    private String msg;

    @Override
    public Integer getValue() {
        return this.code;
    }
}

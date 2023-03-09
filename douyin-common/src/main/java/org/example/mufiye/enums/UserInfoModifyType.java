package org.example.mufiye.enums;

import org.example.mufiye.exception.GraceException;
import org.example.mufiye.result.ResponseStatusEnum;

public enum UserInfoModifyType {
    NICKNAME(1, "昵称"),
    DouyinId(2, "抖音号"),
    SEX(3, "性别"),
    BIRTHDAY(4, "生日"),
    LOCATION(5, "所在地"),
    DESC(6, "简介");

    public final Integer type;
    public final String value;

    UserInfoModifyType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    // 只能修改这些类型
    public static void checkUserInfoTypeIsRight(Integer type) {
        if (type != UserInfoModifyType.NICKNAME.type &&
            type != UserInfoModifyType.DouyinId.type &&
            type != UserInfoModifyType.SEX.type &&
            type != UserInfoModifyType.BIRTHDAY.type &&
            type != UserInfoModifyType.LOCATION.type &&
            type != UserInfoModifyType.DESC.type) {
            GraceException.display(ResponseStatusEnum.USER_INFO_UPDATED_ERROR);
        }
    }
}

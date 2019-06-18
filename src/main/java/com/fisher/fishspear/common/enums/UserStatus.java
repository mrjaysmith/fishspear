package com.fisher.fishspear.common.enums;

import lombok.Data;

public enum UserStatus {

    NORMAL("normal", 0),
    DEL("delete",1);

    private String key;
    private Integer value;

    UserStatus(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValueByKey(String key) {
        for (UserStatus status : UserStatus.values()) {
            if (status.key.equals(key)) {
                return status.value;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}

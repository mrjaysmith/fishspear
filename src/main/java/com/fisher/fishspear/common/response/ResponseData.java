package com.fisher.fishspear.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回数据
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ResponseData<T> {

    public static final ResponseData DEFAULT = new ResponseData();

    public String message;

    public int state = 200;

    public T data;

    public ResponseData() {
    }

    public ResponseData(T data) {
        this.data = data;
    }

    public ResponseData(int state,String message) {
        this.message = message;
        this.state = state;
    }

    public ResponseData(int state,String message,T data) {
        this.message = message;
        this.state = state;
        this.data = data;
    }
}

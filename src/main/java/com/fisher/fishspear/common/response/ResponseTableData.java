package com.fisher.fishspear.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回数据
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class ResponseTableData<T> extends ResponseData<T>  {

    public long count;
    public Integer current;

    public ResponseTableData() {
    }

    public ResponseTableData(T data) {
        super.data = data;
    }
}

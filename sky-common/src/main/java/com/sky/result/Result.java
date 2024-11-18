package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    /**
     * 创建一个成功的Result对象，包含指定的数据
     * 该方法用于当操作成功时，封装返回数据和状态码
     *
     * @param <T>    泛型参数，表示可以传入任意类型的对象
     * @param object 任意类型的对象，表示成功操作后返回的数据
     * @return 包含成功状态码和数据的Result对象
     */
    public static <T> Result<T> success(T object) {
        // 创建一个Result对象，用于封装返回数据和状态码
        Result<T> result = new Result<T>();
        // 设置Result对象的数据为传入的object
        result.data = object;
        // 设置成功状态码为1
        result.code = 1;
        // 返回封装了成功状态码和数据的Result对象
        return result;
    }


    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}

package com.galaxyt.normae.core.exception;

/**
 * 全局异常状态码
 */
public enum GlobalExceptionCode {


    /**
     * 请求成功
     */
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "系统异常"),
    REQUEST_ARGUMENT_EXCEPTION(2, "请求参数异常"),
    USER_LOGIN_STATUS_EXCEPTION(3, "当前用户登陆信息未进行初始化"),
    NOT_FOUND_ENUM(4, "未找到对应枚举类型"),
    FEIGN_ERROR(5, "Feign 接口请求异常"),
    JEDIS_POOL_INIT_FAIL(6, "Jedis Pool 初始化失败"),
    HTTP_REQUEST_ERROR(7, "网络请求异常"),
    FILE_UPLOAD_ERROR(8, "文件上传失败"),

    /**
     * uaa
     */
    ACCOUNT_IS_DISABLED(1001, "帐号已被禁用"),
    USERNAME_IS_NOT_FOUNT(1002, "帐号不存在"),
    PASSWORD_IS_WRONG(1003, "密码输入错误"),
    USERNAME_ALREADY_EXISTS(1004, "用户名已存在"),
    USER_LOGIN_FAILED(1005, "用户授权失败"),
    USER_UPDATE_FAILED(1006, "用户修改失败"),
    USER_DELETE_FAILED(1007, "用户删除失败"),
    ROLES_NOT_FOUND(1008, "获取用户角色失败"),
    PASSWORD_CHANGE_FAILED(1009, "密码更新失败"),
    ROLE_NAME_EXISTS(1010, "角色名已存在"),
    ROLE_MARK_EXISTS(1011, "角色标识已存在"),
    ROLE_ADD_FAILED(1012, "角色新增失败"),
    ROLE_EDIT_FAILED(1013, "角色修改失败"),
    ROLE_HAS_USER_FAILED(1014, "角色已被使用,无法删除"),
    ROLE_DEL_FAILED(1015, "角色删除失败"),
    UPLOAD_USER_ROLE_FAILED(1016, "用户角色关系存证失败"),
    ROLE_QUERY_FAILED(1017, "角色查询失败"),
    AUTHORITY_NOT_FOUND(1018, "权限不存在"),
    AUTHORITY_ALREADY_EXISTS(1019, "权限名已存在"),
    AUTHORITY_DEL_FAILED(1020, "权限删除失败"),
    AUTHORITY_STATUS_FAILED(1021, "权限启用禁用失败"),
    AUTHORITY_ALREADY_USERD(1022, "权限已被分配,无法删除"),
    ADMIN_CANOT_EDIT(1023, "超级管理员权限不允许修改"),

    /**
     * sms
     */
    PHONE_NUMBER_FORMAT_WRONG(2001, "手机号码格式错误"),
    MESSAGE_SEND_ERROR(2002, "短信发送失败"),
    MESSAGE_SEND_FREQUENTLY(2003, "短信发送频繁"),

    ;

    /**
     * 异常代码
     */
    private final int code;

    /**
     * 异常信息
     */
    private final String msg;


    GlobalExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }


    public static GlobalExceptionCode getByCode(int code) {

        GlobalExceptionCode[] values = GlobalExceptionCode.values();

        for (GlobalExceptionCode value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }

        throw new GlobalException(GlobalExceptionCode.NOT_FOUND_ENUM, String.format("未找到 [%s] 对应的异常类型!", code));
    }

}

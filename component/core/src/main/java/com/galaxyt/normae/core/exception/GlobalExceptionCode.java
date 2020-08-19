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

    MD5_ERROR(8, "MD5 Error"),

    FILE_UPLOAD_ERROR(9, "文件上传失败"),

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

    PHONE_NUMBER_FORMAT_WRONG(2001, "手机号码格式错误"),
    MESSAGE_SEND_ERROR(2002, "短信发送失败"),
    MESSAGE_SEND_FREQUENTLY(2003, "短信发送频繁"),
    PHONE_ALREADY_EXISTS(2004, "手机号码已存在"),
    MESSAGE_VERTY_FREQUENTLY(2005, "短信校验失败"),

    FILE_UPLOAD_FAIL(3001, "文件上传失败"),


    UPLOAD_AOS_FAILED(4001, "科学院存证失败"),
    INIT_AOS_FAILED(4002, "科学院初始化失败"),

    MAIL_SEND_FAILED(5001, "邮件发送失败"),
    MAIL_PARSER_FAILED(5002, "邮件解析失败"),
    ATTACHMENT_PARSER_FAILED(5003, "邮件附件解析失败"),

    //--------------------------院士信息code  start-------------------------------------

    ACADEMICIAN_ERROR(7001, "操作失败"),
    ACADELOGERROR(7002, "日志记录失败"),

    ACAD_NOT_FOUND(7003, "院士信息获取失败"),

    //--------------------------院士信息code  end-------------------------------------


    //--------------------------es  start-------------------------------------

    SYNC_ES_ERROR(5001, "导入数据到es异常"),
    ES_ERROR(5002, "ES操作异常"),
    ES_UNQUERY(5003, "ES中未查询到此数据"),
    //--------------------------es  end-------------------------------------

    //--------------------------mail  start-------------------------------------
    NOT_BINDED_EMIAL(8001, "当前用户未绑定邮箱"),
    ONLY_BIND_ONE_EMIAL(8002, "只能绑定一个邮箱"),
    BINDED_BY_OTHERS(8003, "该邮箱已被其他系统账户绑定"),
    //--------------------------mail  end-------------------------------------

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

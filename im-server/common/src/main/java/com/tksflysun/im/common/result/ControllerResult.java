package com.tksflysun.im.common.result;

public class ControllerResult {

    public final static String SUCCESS = "000001";
    public final static String ERROR = "000002";
    /**
     * 用户未登录
     */
    public final static String NO_LOGIN = "000003";
    public final static String NO_LOGIN_MSG = "用户未登录";
    /**
     * 不具有权限
     */
    public final static String NO_AUTHORITY = "000005";
    /**
     * 登录失败
     */
    public final static String LOGIN_FAIL = "000006";

    /**
     * 注册失败
     */
    public final static String REGIST_FAIL_CODE = "000007";
    public final static String REGIST_FAIL_MSG = "注册失败";

    /**
     * 验证码过期
     */
    public final static String VERIFY_CODE_OUT = "000011";
    /**
     * 账号不能为空
     */
    public final static String ACCOUNT_NULL_CODE = "000012";
    public final static String ACCOUNT_NULL_MSG = "账号不能为空";
    /**
     * 手机号格式不正确
     */
    public final static String INVALID_PHONE_CODE = "000013";
    public final static String INVALID_PHONE_MSG = "手机号格式不正确";
    /**
     * 邮箱格式不正确
     */
    public final static String INVALID_EMAIL_CODE = "000014";
    public final static String INVALID_EMAIL_MSG = "邮箱格式不正确";
    /**
     * 验证次数超限
     */
    public final static String CHECK_REJECT_CODE = "000015";
    public final static String CHECK_REJECT_MSG = "验证次数超限，请稍后再试";
    /**
     * 验证码失效
     */
    public final static String CAPTCHA_EXPIRED_CODE = "000016";
    public final static String CAPTCHA_EXPIRED_MSG = "验证码失效";
    /**
     * 验证码不正确
     */
    public final static String CAPTCHA_INVALID_CODE = "000017";
    public final static String CAPTCHA_INVALID_MSG = "验证码不正确";
    /**
     * 邀请码不正确
     */
    public final static String INVITATION_CODE_INVALID_CODE = "000018";
    public final static String INVITATION_CODE_INVALID_MSG = "邀请码不正确";
    /**
     * 密码过去简单
     */
    public final static String WEAK_PASSWORD_CODE = "000019";
    public final static String WEAK_PASSWORD_MSG = "密码过于简单";
    /**
     * 已注册
     */
    public final static String REGISTERED_CODE = "000020";
    public final static String REGISTERED_MSG = "已被注册";
    /**
     * 邀请码不能为空
     */
    public final static String INVITATION_CODE_NULL_CODE = "000021";
    public final static String INVITATION_CODE_NULL_MSG = "邀请码不能为空";

    /**
     * 密码不能为空
     */
    public final static String PWD_NULL_CODE = "000022";
    public final static String PWD_NULL_MSG = "密码不能为空";

    /**
     * 邮箱已存在
     */
    public final static String EMAIL_EXISTED_CODE = "000025";
    public final static String EMAIL_EXISTED_MSG = "邮箱已存在";

    private String code;
    private String msg;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ControllerResult getSuccessResult() {
        return new ControllerResult(ControllerResult.SUCCESS);
    }

    public static ControllerResult getSuccessResult(String msg) {
        return new ControllerResult(ControllerResult.SUCCESS, msg);
    }

    public static ControllerResult getErrorResult() {
        return new ControllerResult(ControllerResult.ERROR);
    }

    public static ControllerResult getErrorResult(String msg) {
        return new ControllerResult(ControllerResult.ERROR, msg);
    }

    public ControllerResult(String code) {
        super();
        this.code = code;
    }

    public ControllerResult(String code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ControllerResult getResult(String code, String msg, Object data) {
        return new ControllerResult(code, msg, data);
    }

    public static ControllerResult getResult(String code, String msg) {
        return new ControllerResult(code, msg);
    }

    public ControllerResult(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ControllerResult() {
        super();
    }
}

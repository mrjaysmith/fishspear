package com.fisher.fishspear.common.exception;

/**
 * @Description 所有业务异常的枚举
 */
public enum BizExceptionEnum {

	/**
	 * 账户异常
	 */
	USER_NOT_EXISTED(102, "此用户不存在"),
	USER_IS_EXIST(103,"用户名已存在"),
	NO_PERMISSION(104,"用户没有审核权限"),
	USER_PASSWORD_WRONG(105,"密码错误"),
	OLD_PASSWORD_WRONG(812,"原密码错误"),
	THE_SAME_PASSWORD(815,"新旧密码相同"),
	USER_IS_FORBIDDEN(811,"账号被禁用"),

	/**
	 * 通用异常
	 */
	PARAMETER_IS_EMPTY(802,"参数为空"),
    PARAMETER_IS_WRONG(805,"参数不存在"),
	DATE_FORMAT_INVALIDATE(803,"日期格式错误"),
	USER_NOT_LOGIN(808,"用户未登录"),
	CHOICE_PARENT_ROLE(206,"未选择父级角色"),
	ROLE_HAS_CHILDREN(809,"角色存在子角色，不能删除"),
	ROLE_IS_USED(810,"角色被使用，不能删除"),

	/**
	 * 文件上传/下载异常
	 */
	UPLOAD_ERROR(821,"文件上传失败"),
	FILE_NOT_FOUND(824,"文件未找到"),
	FILE_READING_ERROR(827,"文件读取出错");



	BizExceptionEnum(int code, String message) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
	}
	
	BizExceptionEnum(int code, String message,String urlPath) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
		this.urlPath = urlPath;
	}

	private int friendlyCode;

	private String friendlyMsg;
	
	private String urlPath;

	public int getCode() {
		return friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
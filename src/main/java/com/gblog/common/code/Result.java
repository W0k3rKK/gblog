package com.gblog.common.code;

import lombok.Data;

/**
 * @author: W0k3rKK
 * @description 状态码
 * @date: 2023/3/23 18:19
 */
@Data
public class Result {
	public static final int SUCCESS = 200;
	public static final int UNAUTHORIZED = 401;
	public static final int NOT_FOUND = 404;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int BAD_GATEWAY = 502;
	public static final int SERVICE_UNAVAILABLE = 503;
	public static final int GATEWAY_TIMEOUT = 504;

	private int code;
	private String message;

	public Object data;

	/**
	 *
	 * @param data
	 * @return 200
	 */
	public static Result succ(Object data) {
		return result(SUCCESS, "操作成功", data);
	}


	/**
	 *
	 * @param msg
	 * @return 401
	 */
	public static Result unAuth(String msg) {
		return result(UNAUTHORIZED, msg, null);
	}

	/**
	 *
	 * @param msg
	 * @return 404
	 */
	public static Result notFound(String msg) {
		return result(NOT_FOUND, msg, null);
	}

	/**
	 *
	 * @param msg
	 * @return 500
	 */
	public static Result serverError(String msg) {
		return result(INTERNAL_SERVER_ERROR, msg, null);
	}

	/**
	 *
	 * @param msg
	 * @return 502
	 */
	public static Result badGateway(String msg) {
		return result(BAD_GATEWAY, msg, null);
	}

	/**
	 *
	 * @param msg
	 * @return 503
	 */
	public static Result serviceUnavailable(String msg) {
		return result(SERVICE_UNAVAILABLE, msg, null);
	}

	/**
	 *
	 * @param msg
	 * @return 504
	 */
	public static Result gatewayTimeout(String msg) {
		return result(GATEWAY_TIMEOUT, msg, null);
	}

	public static Result result(int code, String msg, Object data) {
		Result result = new Result();
		result.setCode(code);
		result.setMessage(msg);
		result.setData(data);
		return result;
	}
}

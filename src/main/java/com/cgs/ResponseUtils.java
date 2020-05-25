package com.cgs;

import com.cgs.constant.ErrorCode;
import com.cgs.constant.Response;

public class ResponseUtils {

    public static Response buildResponseByCode(ErrorCode code,Object data){
        Response response = new Response();
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());
        response.setData(data);
        return response;
    }
}

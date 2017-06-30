package com.rokid.movie.taobao.api.internal.tdc.parser;

import com.rokid.movie.taobao.api.ApiException;
import com.rokid.movie.taobao.api.Constants;
import com.rokid.movie.taobao.api.internal.tdc.TdcResponse;
import com.rokid.movie.taobao.api.internal.util.json.ExceptionErrorListener;
import com.rokid.movie.taobao.api.internal.util.json.JSONReader;
import com.rokid.movie.taobao.api.internal.util.json.JSONValidatingReader;

import java.util.Map;

/**
 * MBP数据回流的结果解析
 *
 * @author haofeng 2013-5-21
 * @date 2013-5-21 上午11:39:51
 */
public class TdcMbpBackParser implements TdcParser {

    public TdcResponse parse(String rsp) throws ApiException {
        TdcResponse rsponse = new TdcResponse();
        JSONReader reader = new JSONValidatingReader(
                new ExceptionErrorListener());
        Object rootObj = reader.read(rsp);

        if (rootObj instanceof Map<?, ?>) {
            Map<?, ?> rootJson = (Map<?, ?>) rootObj;
            if (rootJson.containsKey(Constants.ERROR_RESPONSE)) {
                Map<?, ?> result = (Map<?, ?>) rootJson.get(Constants.ERROR_RESPONSE);
                rsponse.setErrorCode((String) result.get(Constants.ERROR_CODE));
                rsponse.setMsg((String) result.get(Constants.ERROR_MSG));
                rsponse.setSubCode((String) result.get(Constants.ERROR_SUB_CODE));
                rsponse.setSubMsg((String) result.get(Constants.ERROR_SUB_MSG));
            }
        }

        rsponse.setBody(rsp);
        return rsponse;
    }
}

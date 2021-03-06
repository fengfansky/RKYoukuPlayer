package com.rokid.movie.taobao.api.request;

import com.rokid.movie.taobao.api.ApiRuleException;
import com.rokid.movie.taobao.api.BaseTaobaoRequest;
import com.rokid.movie.taobao.api.internal.util.TaobaoHashMap;
import com.rokid.movie.taobao.api.response.YunosTvsearchCheckinstallappGetResponse;

import java.util.Map;

/**
 * TOP API: yunos.tvsearch.checkinstallapp.get request
 *
 * @author top auto create
 * @since 1.0, 2017.03.22
 */
public class CheckinstallAppRequest extends BaseTaobaoRequest<YunosTvsearchCheckinstallappGetResponse> {


    public String getApiMethodName() {
        return "yunos.tvsearch.checkinstallapp.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public Class<YunosTvsearchCheckinstallappGetResponse> getResponseClass() {
        return YunosTvsearchCheckinstallappGetResponse.class;
    }

    public void check() throws ApiRuleException {

    }
}

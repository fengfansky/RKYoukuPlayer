package com.rokid.movie.taobao.api.request;

import com.rokid.movie.taobao.api.ApiRuleException;
import com.rokid.movie.taobao.api.BaseTaobaoRequest;
import com.rokid.movie.taobao.api.internal.util.TaobaoHashMap;
import com.rokid.movie.taobao.api.response.YoukuResponse;

import java.util.Map;

/**
 * Created by lizhi.ywp on 2017-05-17.
 */

public class YoukuRequest extends BaseTaobaoRequest<YoukuResponse> {
    /**
     * 搜索条件对象
     */
    private String show_id;

    @Override
    public String getApiMethodName() {
        return "youku.tv.show.get";
    }

    public void setShowId(String showId) {
        this.show_id = showId;
    }

    @Override
    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("show_id", this.show_id);
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    @Override
    public Class<YoukuResponse> getResponseClass() {
        return YoukuResponse.class;
    }

    @Override
    public void check() throws ApiRuleException {

    }
}

package com.taobao.api.response;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

import java.util.List;

/**
 * TOP API: yunos.tvsearch.checkinstallapp.get response.
 *
 * @author top auto create
 * @since 1.0, null
 */
public class YunosTvsearchCheckinstallappGetResponse extends TaobaoResponse {

    private static final long serialVersionUID = 4568215717282841679L;

    /**
     * 结果
     */
    @ApiListField("result_list")
    @ApiField("string")
    private List<String> resultList;


    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }

    public List<String> getResultList() {
        return this.resultList;
    }


    @Override
    public String toString() {
        return "YunosTvsearchCheckinstallappGetResponse{" +
                "resultList=" + ((null != resultList) ? resultList.toString() : "") +
                '}';
    }
}

package com.taobao.api;


public interface TaobaoCallback<T extends TaobaoResponse> {

    /**
     * 当返回成功消息
     *
     * @param resp
     */
    void onSuccess(T rsp);


    /**
     * @param msg
     */
    void onError(T rsp, String msg);
}

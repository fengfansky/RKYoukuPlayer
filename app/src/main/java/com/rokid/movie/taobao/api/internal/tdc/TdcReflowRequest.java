package com.rokid.movie.taobao.api.internal.tdc;

import com.rokid.movie.taobao.api.internal.util.TaobaoHashMap;
import com.rokid.movie.taobao.api.internal.util.json.JSONValidatingWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TdcReflowRequest,TDC数据回流的请求类
 *
 * @author haofeng 2013-1-15
 * @date 2013-1-15 上午9:47:22
 * {@link}详细说明可参照TDC说明文档
 */
public class TdcReflowRequest {
    public static final String P_INSERT_VALUE = "contain";
    public static final String P_INSERT_TYPE = "insert_type";
    public static final String P_INSERT = "insert";
    public static final String PATH_INFO = "path_info";
    public static final String P_MODE = "mode";
    public static final String INSERT_TYPE_BATCH = "batch_insert";

    /**
     * TDC查询的参数pathInfo:TDC查询路径
     */
    private String pathInfo;

    /**
     * 插入的值的List
     */
    private List<Map<String, String>> values = new ArrayList<Map<String, String>>();

    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public void setValues(List<Map<String, String>> values) {
        this.values = values;
    }

    public TaobaoHashMap getParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();

        if (values != null && !values.isEmpty()) {
            JSONValidatingWriter writer = new JSONValidatingWriter();
            String value = writer.write(values);
            String table = writer.write(pathInfo.substring(1, pathInfo.length()));
            txtParams.put(P_INSERT_VALUE, "{" + table + ":" + value + "}");
        }

        txtParams.put(P_INSERT_TYPE, INSERT_TYPE_BATCH);
        txtParams.put(P_MODE, TdcRequestMode.MODE_ODATA_INSERT);
        return txtParams;
    }

    /**
     * 设置查询的pathInfo属性
     *
     * @param pathInfo
     * @return
     */
    public TdcReflowRequest pathInfo(String pathInfo) {
        if (!pathInfo.startsWith("/")) {
            pathInfo = "/" + pathInfo;
        }
        this.pathInfo = pathInfo;
        return this;
    }

    public TdcReflowRequest values(List<Map<String, String>> values) {
        this.values = values;
        return this;
    }
}

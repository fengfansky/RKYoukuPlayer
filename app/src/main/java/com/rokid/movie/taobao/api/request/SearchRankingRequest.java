package com.rokid.movie.taobao.api.request;

import com.rokid.movie.taobao.api.ApiRuleException;
import com.rokid.movie.taobao.api.BaseTaobaoRequest;
import com.rokid.movie.taobao.api.TaobaoObject;
import com.rokid.movie.taobao.api.internal.mapping.ApiField;
import com.rokid.movie.taobao.api.internal.util.TaobaoHashMap;
import com.rokid.movie.taobao.api.internal.util.json.JSONWriter;
import com.rokid.movie.taobao.api.response.YunosTvsearchRankingGetResponse;

import java.util.Map;

/**
 * TOP API: yunos.tvsearch.ranking.get request
 *
 * @author top auto create
 * @since 1.0, 2017.03.23
 */
public class SearchRankingRequest extends BaseTaobaoRequest<YunosTvsearchRankingGetResponse> {


    /**
     * 接口条件对象
     */
    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQuery(RankingVideoQuery query) {
        this.query = new JSONWriter(false, true).write(query);
    }

    public String getQuery() {
        return this.query;
    }

    public String getApiMethodName() {
        return "yunos.tvsearch.ranking.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("query", this.query);
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public Class<YunosTvsearchRankingGetResponse> getResponseClass() {
        return YunosTvsearchRankingGetResponse.class;
    }

    public void check() throws ApiRuleException {
    }

    /**
     * 接口条件对象
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class RankingVideoQuery extends TaobaoObject {

        private static final long serialVersionUID = 7331938685518898672L;

        /**
         * 区域语言
         */
        @ApiField("locale_info")
        private String localeInfo;
        /**
         * 应用版本
         */
        @ApiField("package_info")
        private String packageInfo;
        /**
         * 应用标识，一般用包名替代
         */
        @ApiField("package_name")
        private String packageName;
        /**
         * 应用的页面名，不同页面可能对应不同的搜推荐资源
         */
        @ApiField("page_name")
        private String pageName;
        /**
         * 设备信息
         */
        @ApiField("system_info")
        private String systemInfo;
        /**
         * 应用版本号 暂时无用，可传可不传
         */
        @ApiField("version")
        private String version;


        public String getLocaleInfo() {
            return this.localeInfo;
        }

        public void setLocaleInfo(String localeInfo) {
            this.localeInfo = localeInfo;
        }

        public String getPackageInfo() {
            return this.packageInfo;
        }

        public void setPackageInfo(String packageInfo) {
            this.packageInfo = packageInfo;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPageName() {
            return this.pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public String getSystemInfo() {
            return this.systemInfo;
        }

        public void setSystemInfo(String systemInfo) {
            this.systemInfo = systemInfo;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

    }


}
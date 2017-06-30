package com.rokid.movie.taobao.api.request;

import com.rokid.movie.taobao.api.ApiRuleException;
import com.rokid.movie.taobao.api.BaseTaobaoRequest;
import com.rokid.movie.taobao.api.TaobaoObject;
import com.rokid.movie.taobao.api.internal.mapping.ApiField;
import com.rokid.movie.taobao.api.internal.util.TaobaoHashMap;
import com.rokid.movie.taobao.api.internal.util.json.JSONWriter;
import com.rokid.movie.taobao.api.response.YunosTvsearchDataSearchResponse;

import java.util.Map;

/**
 * TOP API: yunos.tvsearch.data.search request
 *
 * @author top auto create
 * @since 1.0, 2017.03.23
 */
public class SearchRequest extends BaseTaobaoRequest<YunosTvsearchDataSearchResponse> {


    /**
     * 入参对象
     */
    private String query;

    public void setQuery(String query) {
        this.query = query;
    }

    public void setQuery(TvSearchQuery query) {
        this.query = new JSONWriter(false, true).write(query);
    }

    public String getQuery() {
        return this.query;
    }

    public String getApiMethodName() {
        return "yunos.tvsearch.data.search";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("query", this.query);
        if (this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public Class<YunosTvsearchDataSearchResponse> getResponseClass() {
        return YunosTvsearchDataSearchResponse.class;
    }

    public void check() throws ApiRuleException {
    }

    /**
     * 业务扩展字段
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class Bizext extends TaobaoObject {

        private static final long serialVersionUID = 7653761478136714913L;

        /**
         * 生日，格式:yyyy-MM-dd
         */
        @ApiField("birthday")
        private String birthday;
        /**
         * 阿里影视特有参数：类目ID，多个ID用英文逗号分割
         */
        @ApiField("cate_ids")
        private String cateIds;
        /**
         * 性别1-female，2-male
         */
        @ApiField("gender")
        private String gender;
        /**
         * 阿里影视特有参数：搜索方式,0-普通(不转换，是啥搜啥)|1-T9(数字)（键盘上的每个键的对应的几个字母数字）|2-首拼|3-笔画（根据笔画顺序搜索），默认0
         */
        @ApiField("key_type")
        private String keyType;
        /**
         * 设备内存，单位M
         */
        @ApiField("memory")
        private String memory;
        /**
         * 是否是家长， 1是家长,
         */
        @ApiField("parent")
        private String parent;
        /**
         * 阿里影视特有参数：影视节目类型 null全部, 0咨询，电影，3电视剧，4综艺
         */
        @ApiField("show_type")
        private String showType;
        /**
         * 商品特有参数：商品搜索排序类型。类型包括： s: 默认排序 p: 价格从低到高; pd: 价格从高到低; d: 月销量从高到低; td: 总销量从高到低
         */
        @ApiField("tb_sort")
        private String tbSort;
        /**
         * 虾米特有参数：是否发布, 默认true
         */
        @ApiField("xm_pub")
        private String xmPub;


        public String getBirthday() {
            return this.birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCateIds() {
            return this.cateIds;
        }

        public void setCateIds(String cateIds) {
            this.cateIds = cateIds;
        }

        public String getGender() {
            return this.gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getKeyType() {
            return this.keyType;
        }

        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }

        public String getMemory() {
            return this.memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getParent() {
            return this.parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getShowType() {
            return this.showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getTbSort() {
            return this.tbSort;
        }

        public void setTbSort(String tbSort) {
            this.tbSort = tbSort;
        }

        public String getXmPub() {
            return this.xmPub;
        }

        public void setXmPub(String xmPub) {
            this.xmPub = xmPub;
        }

    }

    /**
     * 入参对象
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class TvSearchQuery extends TaobaoObject {

        private static final long serialVersionUID = 2424431165736448879L;

        /**
         * 进入搜索的应用标识，一般用当前应用包名替代
         */
        @ApiField("app")
        private String app;
        /**
         * 业务扩展字段
         */
        @ApiField("biz_ext")
        private Bizext bizExt;
        /**
         * 请求来源
         */
        @ApiField("from")
        private String from;
        /**
         * 搜索的关键字，支持拼音首字母，全拼，汉字
         */
        @ApiField("keyword")
        private String keyword;
        /**
         * 表示区域语言
         */
        @ApiField("locale_info")
        private String localeInfo;
        /**
         * 应用安装信息
         */
        @ApiField("package_info")
        private String packageInfo;
        /**
         * 当前页码
         */
        @ApiField("page")
        private Long page;
        /**
         * 每页返回个数
         */
        @ApiField("page_size")
        private Long pageSize;
        /**
         * 每次搜索标识
         */
        @ApiField("search_id")
        private String searchId;
        /**
         * 是否是拼音，首拼和全拼都是拼音
         */
        @ApiField("spell")
        private Boolean spell;
        /**
         * 设备信息
         */
        @ApiField("system_info")
        private String systemInfo;
        /**
         * UUID
         */
        @ApiField("uuid")
        private String uuid;


        public String getApp() {
            return this.app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public Bizext getBizExt() {
            return this.bizExt;
        }

        public void setBizExt(Bizext bizExt) {
            this.bizExt = bizExt;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

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

        public Long getPage() {
            return this.page;
        }

        public void setPage(Long page) {
            this.page = page;
        }

        public Long getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(Long pageSize) {
            this.pageSize = pageSize;
        }

        public String getSearchId() {
            return this.searchId;
        }

        public void setSearchId(String searchId) {
            this.searchId = searchId;
        }

        public Boolean getSpell() {
            return this.spell;
        }

        public void setSpell(Boolean spell) {
            this.spell = spell;
        }

        public String getSystemInfo() {
            return this.systemInfo;
        }

        public void setSystemInfo(String systemInfo) {
            this.systemInfo = systemInfo;
        }

        public String getUuid() {
            return this.uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

    }


}
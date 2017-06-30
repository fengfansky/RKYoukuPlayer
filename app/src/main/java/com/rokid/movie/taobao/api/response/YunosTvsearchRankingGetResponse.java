package com.rokid.movie.taobao.api.response;

import com.rokid.movie.taobao.api.TaobaoObject;
import com.rokid.movie.taobao.api.TaobaoResponse;
import com.rokid.movie.taobao.api.internal.mapping.ApiField;
import com.rokid.movie.taobao.api.internal.mapping.ApiListField;

import java.util.List;

/**
 * TOP API: yunos.tvsearch.ranking.get response.
 *
 * @author top auto create
 * @since 1.0, null
 */
public class YunosTvsearchRankingGetResponse extends TaobaoResponse {

    private static final long serialVersionUID = 2494228145287684197L;

    /**
     * 返回结果
     */
    @ApiField("result")
    private Result result;


    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return this.result;
    }

    /**
     * data
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class Data extends TaobaoObject {

        private static final long serialVersionUID = 5888483492877922668L;

        /**
         * 别名
         */
        @ApiField("alias_name")
        private String aliasName;
        /**
         * 地区
         */
        @ApiField("area")
        private String area;
        /**
         * 人物对象的：生日，yyyy-MM-dd
         */
        @ApiField("birthday")
        private String birthday;
        /**
         * 商品后台类目id
         */
        @ApiField("cat_id")
        private Long catId;
        /**
         * 人物对象的：祭日，yyyy-MM-dd
         */
        @ApiField("die_day")
        private String dieDay;
        /**
         * 下载次数
         */
        @ApiField("download_times")
        private String downloadTimes;
        /**
         * enName
         */
        @ApiField("en_name")
        private String enName;
        /**
         * 商品邮费
         */
        @ApiField("fast_post_fee")
        private String fastPostFee;
        /**
         * 性别 0男，1,女，2混合,3未知
         */
        @ApiField("gender")
        private String gender;
        /**
         * 籍贯
         */
        @ApiField("homeplace")
        private String homeplace;
        /**
         * 内容ID：影视ID，音乐ID，应用ID等
         */
        @ApiField("id")
        private String id;
        /**
         * 动画明星简介
         */
        @ApiField("introduction")
        private String introduction;
        /**
         * 是否货到付款: 1表示货到付款
         */
        @ApiField("is_cod")
        private Long isCod;
        /**
         * 商品是否有折扣。true表示有折扣，false表示无折扣
         */
        @ApiField("is_promotion")
        private Boolean isPromotion;
        /**
         * 职业
         */
        @ApiField("job")
        private String job;
        /**
         * 关键词
         */
        @ApiField("keyword")
        private String keyword;
        /**
         * 角标
         */
        @ApiField("label")
        private String label;
        /**
         * 角标内容
         */
        @ApiField("label_name")
        private String labelName;
        /**
         * 角标类型
         */
        @ApiField("label_type")
        private String labelType;
        /**
         * 商品所在地
         */
        @ApiField("location")
        private String location;
        /**
         * 搜索匹配的词的属性名
         */
        @ApiField("match_slot")
        private String matchSlot;
        /**
         * 搜索匹配的词
         */
        @ApiField("match_word")
        private String matchWord;
        /**
         * 民族
         */
        @ApiField("nation")
        private String nation;
        /**
         * 卖家昵称
         */
        @ApiField("nick")
        private String nick;
        /**
         * 应用包名
         */
        @ApiField("package_name")
        private String packageName;
        /**
         * 动画明背景图
         */
        @ApiField("pic_bg")
        private String picBg;
        /**
         * 内容图片
         */
        @ApiField("pic_url")
        private String picUrl;
        /**
         * sku最低价（原价）
         */
        @ApiField("price")
        private String price;
        /**
         * 商品sku最低价的折扣价。如果无折扣，值与price相同。
         */
        @ApiField("price_with_rate")
        private String priceWithRate;
        /**
         * 埋点用: 推荐平台的scm标记，包含推荐场景、方案信息
         */
        @ApiField("scm")
        private String scm;
        /**
         * 评分
         */
        @ApiField("score")
        private String score;
        /**
         * 店铺所在地
         */
        @ApiField("seller_loc")
        private String sellerLoc;
        /**
         * 是否免邮: -1表示免邮
         */
        @ApiField("shipping")
        private Long shipping;
        /**
         * 影视类型 0咨询，1电影，3电视剧，4综艺
         */
        @ApiField("show_type")
        private String showType;
        /**
         * 歌手
         */
        @ApiField("singer")
        private String singer;
        /**
         * 销售量
         */
        @ApiField("sold")
        private String sold;
        /**
         * 商品所属spu id
         */
        @ApiField("spu_id")
        private Long spuId;
        /**
         * 提示
         */
        @ApiField("tips")
        private String tips;
        /**
         * 内容名称：电影名，歌曲名，应用名等
         */
        @ApiField("title")
        private String title;
        /**
         * 客户端跳转的uri
         */
        @ApiField("uri")
        private String uri;
        /**
         * 卖家ID
         */
        @ApiField("user_id")
        private String userId;
        /**
         * 权重值
         */
        @ApiField("weight")
        private Long weight;
        /**
         * 商品sku最低价的折扣价。如果无折扣，值与price相同。
         */
        @ApiField("wm_price")
        private String wmPrice;


        public String getAliasName() {
            return this.aliasName;
        }

        public void setAliasName(String aliasName) {
            this.aliasName = aliasName;
        }

        public String getArea() {
            return this.area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBirthday() {
            return this.birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public Long getCatId() {
            return this.catId;
        }

        public void setCatId(Long catId) {
            this.catId = catId;
        }

        public String getDieDay() {
            return this.dieDay;
        }

        public void setDieDay(String dieDay) {
            this.dieDay = dieDay;
        }

        public String getDownloadTimes() {
            return this.downloadTimes;
        }

        public void setDownloadTimes(String downloadTimes) {
            this.downloadTimes = downloadTimes;
        }

        public String getEnName() {
            return this.enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public String getFastPostFee() {
            return this.fastPostFee;
        }

        public void setFastPostFee(String fastPostFee) {
            this.fastPostFee = fastPostFee;
        }

        public String getGender() {
            return this.gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getHomeplace() {
            return this.homeplace;
        }

        public void setHomeplace(String homeplace) {
            this.homeplace = homeplace;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntroduction() {
            return this.introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public Long getIsCod() {
            return this.isCod;
        }

        public void setIsCod(Long isCod) {
            this.isCod = isCod;
        }

        public Boolean getIsPromotion() {
            return this.isPromotion;
        }

        public void setIsPromotion(Boolean isPromotion) {
            this.isPromotion = isPromotion;
        }

        public String getJob() {
            return this.job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabelName() {
            return this.labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getLabelType() {
            return this.labelType;
        }

        public void setLabelType(String labelType) {
            this.labelType = labelType;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMatchSlot() {
            return this.matchSlot;
        }

        public void setMatchSlot(String matchSlot) {
            this.matchSlot = matchSlot;
        }

        public String getMatchWord() {
            return this.matchWord;
        }

        public void setMatchWord(String matchWord) {
            this.matchWord = matchWord;
        }

        public String getNation() {
            return this.nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getNick() {
            return this.nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPicBg() {
            return this.picBg;
        }

        public void setPicBg(String picBg) {
            this.picBg = picBg;
        }

        public String getPicUrl() {
            return this.picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceWithRate() {
            return this.priceWithRate;
        }

        public void setPriceWithRate(String priceWithRate) {
            this.priceWithRate = priceWithRate;
        }

        public String getScm() {
            return this.scm;
        }

        public void setScm(String scm) {
            this.scm = scm;
        }

        public String getScore() {
            return this.score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getSellerLoc() {
            return this.sellerLoc;
        }

        public void setSellerLoc(String sellerLoc) {
            this.sellerLoc = sellerLoc;
        }

        public Long getShipping() {
            return this.shipping;
        }

        public void setShipping(Long shipping) {
            this.shipping = shipping;
        }

        public String getShowType() {
            return this.showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getSinger() {
            return this.singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public String getSold() {
            return this.sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
        }

        public Long getSpuId() {
            return this.spuId;
        }

        public void setSpuId(Long spuId) {
            this.spuId = spuId;
        }

        public String getTips() {
            return this.tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUri() {
            return this.uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getWeight() {
            return this.weight;
        }

        public void setWeight(Long weight) {
            this.weight = weight;
        }

        public String getWmPrice() {
            return this.wmPrice;
        }

        public void setWmPrice(String wmPrice) {
            this.wmPrice = wmPrice;
        }

    }

    /**
     * 返回结果，内容列表
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class Model extends TaobaoObject {

        private static final long serialVersionUID = 4519534991892417728L;

        /**
         * 当前页码
         */
        @ApiField("cur_page")
        private Long curPage;
        /**
         * 是否有下一页
         */
        @ApiField("has_next")
        private Boolean hasNext;
        /**
         * data
         */
        @ApiListField("list")
        @ApiField("data")
        private List<Data> list;
        /**
         * 更多uri
         */
        @ApiField("more_uri")
        private String moreUri;
        /**
         * 每页返回个数
         */
        @ApiField("page_size")
        private Long pageSize;
        /**
         * 排序
         */
        @ApiField("sort")
        private Long sort;
        /**
         * 排行资源标识：VIDEO_ALL_HOT热门影视，VIDEO_MOVIE_HOT热门电影，VIDEO_SERIAL_HOT热门电视剧，VIDEO_VARIETY_HOT热门综艺，VIDEO_CARTOON_HOT热门动漫，VIDEO_DOCUMENTARY_HOT热门记录片，VIDEO_NEWSREEL_HOT热门资讯，VIDEO_MV_HOT热门音乐视频，VIDEO_EDU_HOT热门教育视频，VIDEO_TRAINING_HOT热门课程培训视频
         */
        @ApiField("source")
        private String source;
        /**
         * 排行名称
         */
        @ApiField("title")
        private String title;
        /**
         * 总记录数数
         */
        @ApiField("total")
        private Long total;
        /**
         * 总页数
         */
        @ApiField("total_page")
        private Long totalPage;
        /**
         * 数据类型
         */
        @ApiField("type")
        private String type;


        public Long getCurPage() {
            return this.curPage;
        }

        public void setCurPage(Long curPage) {
            this.curPage = curPage;
        }

        public Boolean getHasNext() {
            return this.hasNext;
        }

        public void setHasNext(Boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<Data> getList() {
            return this.list;
        }

        public void setList(List<Data> list) {
            this.list = list;
        }

        public String getMoreUri() {
            return this.moreUri;
        }

        public void setMoreUri(String moreUri) {
            this.moreUri = moreUri;
        }

        public Long getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(Long pageSize) {
            this.pageSize = pageSize;
        }

        public Long getSort() {
            return this.sort;
        }

        public void setSort(Long sort) {
            this.sort = sort;
        }

        public String getSource() {
            return this.source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getTotal() {
            return this.total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getTotalPage() {
            return this.totalPage;
        }

        public void setTotalPage(Long totalPage) {
            this.totalPage = totalPage;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    /**
     * 返回结果
     *
     * @author top auto create
     * @since 1.0, null
     */
    public static class Result extends TaobaoObject {

        private static final long serialVersionUID = 5468582235597933742L;

        /**
         * 返回结果，内容列表
         */
        @ApiListField("list")
        @ApiField("model")
        private List<Model> list;


        public List<Model> getList() {
            return this.list;
        }

        public void setList(List<Model> list) {
            this.list = list;
        }

    }


}

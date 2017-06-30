package com.rokid.movie.taobao.api.request;

import com.rokid.movie.bean.base.BaseBean;


/**
 * Created by fanfeng on 2017/5/15.
 */

public class TvSearchQuery extends BaseBean {

    private static final String TAG = "yunos";

    private int page;
    private int page_size;
    private String app;
    private String keyword;
    private boolean spell;
    private String system_info;
    private String locale_info;
    private String uuid;
    private String package_info;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isSpell() {
        return spell;
    }

    public void setSpell(boolean spell) {
        this.spell = spell;
    }

    public String getSystem_info() {
        return system_info;
    }

    public void setSystem_info(String system_info) {
        this.system_info = system_info;
    }

    public String getLocale_info() {
        return locale_info;
    }

    public void setLocale_info(String locale_info) {
        this.locale_info = locale_info;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPackage_info() {
        return package_info;
    }

    public void setPackage_info(String package_info) {
        this.package_info = package_info;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

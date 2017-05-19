package com.youku.bean.base;

import com.google.gson.Gson;

/**
 * Description: TODO
 * Author: xupan.shi
 * Version: V0.1 2017/3/14
 */
public class BaseBean {

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

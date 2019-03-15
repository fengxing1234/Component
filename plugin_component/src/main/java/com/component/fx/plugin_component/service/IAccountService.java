package com.component.fx.plugin_component.service;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 接口中定义了 Login 组件向外提供的数据传递的接口方法
 */
public interface IAccountService {

    //是否已经登录
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     *
     * @return
     */
    String getAccountId();

    /**
     * 获取UserFragment实例
     * 主项目如何在不直接访问组件中具体类的情况下使用组件的 Fragment
     *
     * @return
     */
    Fragment newUserFragment(Activity activity, int containerId, FragmentManager fragmentManager, Bundle bundle, String tag);

}

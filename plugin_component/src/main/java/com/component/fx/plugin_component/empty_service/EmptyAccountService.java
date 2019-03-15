package com.component.fx.plugin_component.empty_service;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.component.fx.plugin_component.service.IAccountService;

/**
 * 提供空实现，在组件单独调试或部分集成调试时避免出现由于实现类对象为空引起的空指针异常。
 */
public class EmptyAccountService implements IAccountService {


    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }

    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager fragmentManager, Bundle bundle, String tag) {
        return null;
    }

}

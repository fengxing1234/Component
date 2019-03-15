package com.component.fx.plugin_login.export;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.component.fx.plugin_component.service.IAccountService;
import com.component.fx.plugin_login.UserFragment;

public class ExportAccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String getAccountId() {
        return "11111111";
    }

    @Override
    public Fragment newUserFragment(Activity activity, int containerId, FragmentManager fragmentManager, Bundle bundle, String tag) {
        Fragment fragment = UserFragment.newInstance(bundle);
        fragmentManager.beginTransaction().add(containerId, fragment, tag).commit();
        return fragment;
    }

}

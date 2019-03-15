package com.component.fx.plugin_login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class UserFragment extends Fragment {

    public static Fragment newInstance(Bundle bundle) {
        UserFragment userFragment = new UserFragment();
        userFragment.setArguments(bundle);
        return userFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_user, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString("1");
            String str = arguments.getString("2");
            Toast.makeText(getContext(), string + "==" + str, Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

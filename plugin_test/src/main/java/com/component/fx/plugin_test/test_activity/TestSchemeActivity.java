package com.component.fx.plugin_test.test_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.component.fx.plugin_test.R;

/**
 * URI的结构：
 * < scheme>://< host>:< port>/[< path>|< pathPrefix>|< pathPattern>]
 * <p>
 * 而对于path ,pathPrefix ,pathPattern只要有一个正确就能够完全匹配整个URI了
 * <p>
 * <p>
 * 短信：
 * 短信使用短链接 <intent-filter>中加入自定义action 会失效 命中失败
 * <p>
 * APP Link
 * <p>
 * 1. scheme 必须是http 或者 https的
 * 2. android:autoVerify="true"验证点击的链接和APP是否有关联
 * 3. 创建服务端文件和APP关联 在安装的时候会去校验这个文件，校验文件上声明的应用包名、文件所在的域名、以及文件声明的APP密钥，是否能和app中的配置匹配上，如果匹配上了，在点击该域名下的任何链接的时候，都会直接定向到我们的APP。
 * 4. 这个文件的格式的content-type必须是application/json
 * 5. 这个文件只能放在https的链接中，不管你之前在action中声明的是http或者https
 * 6. 这个文件不能有任何重定向，并且必须是以/.well-known/assetlinks.json 后缀结尾，注意看我上传的文件示例
 * 7. 你也可以在这个文件上声明多个APP,注意看它的格式，是一个list
 */
public class TestSchemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_scheme_activity);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }


}

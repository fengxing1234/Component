package com.component.fx.plugin_news.network;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@StringDef({NewsType.TOP, NewsType.CAI_JING, NewsType.GUO_JI, NewsType.GUO_NEI, NewsType.JUN_SHI, NewsType.KE_JI, NewsType.SHE_HUI, NewsType.SHI_SHANG, NewsType.TI_YU, NewsType.YU_LE})
@Retention(RetentionPolicy.SOURCE)
public @interface NewsType {
    String TOP = "top";

    String SHE_HUI = "shehui";

    String GUO_NEI = "guonei";

    String GUO_JI = "guoji";

    String YU_LE = "yule";

    String TI_YU = "tiyu";

    String JUN_SHI = "junshi";

    String KE_JI = "keji";

    String CAI_JING = "caijing";

    String SHI_SHANG = "shishang";

}

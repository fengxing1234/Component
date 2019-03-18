package com.component.fx.plugin_news.network;

public enum NewsEnum {


    TOP("top", "头条"),

    SHE_HUI("shehui", "社会"),

    GUO_NEI("guonei", "国内"),

    GUO_JI("guoji", "国际"),

    YU_LE("yule", "娱乐"),

    TI_YU("tiyu", "体育"),

    JUN_SHI("junshi", "军事"),

    KE_JI("keji", "科技"),

    CAI_JING("caijing", "财经"),

    SHI_SHANG("shishang", "时尚");

    private final String mName;
    private String mType;

    NewsEnum(String type, String name) {
        this.mType = type;
        this.mName = name;
    }

    public String getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public static String getTypeByName(String name) {
        for (NewsEnum newsEnum : NewsEnum.values()) {
            if (newsEnum.getName().equals(name)) {
                return newsEnum.getType();
            }
        }
        return "";
    }

    public static NewsEnum getEnumByName(String name) {
        for (NewsEnum newsEnum : NewsEnum.values()) {
            if (newsEnum.getName().equals(name)) {
                return newsEnum;
            }
        }
        return NewsEnum.TOP;
    }
}

package com.component.fx.plugin_news.network;

public enum NewsEnum {


    TOP("top"),

    SHE_HUI("shehui"),

    GUO_NEI("guonei"),

    GUO_JI("guoji"),

    YU_LE("yule"),

    TI_YU("tiyu"),

    JUN_SHI("junshi"),

    KE_JI("keji"),

    CAI_JING("caijing"),

    SHI_SHANG("shishang");

    private String mType;

    NewsEnum(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }
}

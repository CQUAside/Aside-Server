package com.round.aside.server.enumeration;

/**
 * 广告状态操作枚举。<br>
 * 分别为上架，下架，删除，审核
 * 
 * @author A Shuai
 * @date 2016-6-21
 * 
 */
public enum AdStatusOpeEnum {

    /**
     * 上架
     */
    PUTAWAY(0),

    /**
     * 下架
     */
    UNSHELVE(1),

    /**
     * 删除
     */
    DELETE(2),

    /**
     * 审核
     */
    CHECK(3);

    // 类型参数
    private final int type;

    private AdStatusOpeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 根据指定类型参数转换为对应的广告状态枚举值
     * 
     * @param type
     *            类型参数
     * @return
     */
    public static AdStatusOpeEnum valueOf(int type) {
        for (AdStatusOpeEnum mItem : values()) {
            if (mItem.getType() == type) {
                return mItem;
            }
        }
        throw new IllegalArgumentException("Illegal type parameter!");
    }

}

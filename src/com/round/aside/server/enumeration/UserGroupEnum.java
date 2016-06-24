package com.round.aside.server.enumeration;

/**
 * 用户组枚举。<br>
 * 分别为普通用户、管理员组等
 * 
 * @author A Shuai
 * @date 2016-6-23
 * 
 */
public enum UserGroupEnum {

    /**
     * 普通用户
     */
    USER(1),

    /**
     * 管理员
     */
    ADMIN(2);

    private final int type;

    private UserGroupEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 根据指定类型参数转换为对应的用户组枚举
     * 
     * @param type
     *            类型参数
     * @return 对应的用户组权限
     */
    public static UserGroupEnum valueOf(int type) {
        for (UserGroupEnum mItem : values()) {
            if (mItem.getType() == type) {
                return mItem;
            }
        }
        throw new IllegalArgumentException("Illegal type parameter!");
    }

}

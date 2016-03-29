package com.round.aside.server.module.generator;

import com.round.aside.server.module.IModule;

/**
 * ID生成器模块的超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public interface IGenerator extends IModule {

    /**
     * 生成一个UserID，随机值取值范围为<code>1-99999999</code>
     * 
     * @param mInitSeed
     *            初始种子，具体实现可用可不用
     * @return 随机生成的UserID
     */
    int generateUserID(int mInitSeed);

    /**
     * 生成一个图片ID，图片ID的生成格式为“年月日小时分钟秒+8位UserID+4位随机数”。 末尾的随机数生成范围为
     * <code>1-9999</code>。例如生成结果为<code>20160104103055235+00000001+0135</code>。
     * 凡是不足位数的要补零凑足。
     * 
     * @param mUserID
     *            用户ID
     * @param mInitSeed
     *            初始种子，具体实现可用可不用
     * @return 随机生成的图片ID
     */
    String generatePicID(int mUserID, int mInitSeed);

}

package com.round.aside.server.module.generator;

import com.round.aside.server.module.IModule;
import com.round.aside.server.module.IRecyclableModule;

/**
 * 生成器模块的超级接口
 * 
 * @author A Shuai
 * @date 2016-3-28
 * 
 */
public interface IGenerator extends IModule, IRecyclableModule<IGenerator> {

    /**
     * 随机生成一个UserID，随机值取值范围为大于0的int整形数<code>1-2147483648</code>
     * 
     * @param mInitSeed
     *            初始种子，具体实现可用可不用。注：指定为0时，表示不指定初始种子，即一个无效种子
     * @return 随机生成的UserID
     */
    int generateUserID(long mInitSeed);

    /**
     * 生成一个图片ID，图片ID的生成格式为“年月日小时分钟秒+10位UserID+4位随机数”。 末尾的随机数生成范围为
     * <code>1-9999</code>。例如生成结果为<code>20160104103055235+0000000001+0135</code>。
     * 凡是不足位数的要补零凑足。
     * 
     * @param mUserID
     *            用户ID
     * @param mInitSeed
     *            初始种子，具体实现可用可不用。注：指定为0时，表示不指定初始种子，即一个无效种子
     * @return 随机生成的图片ID
     */
    String generatePicID(int mUserID, long mInitSeed);

    /**
     * 随机生成一个Token令牌。可使用用户ID及指定时间参与计算生成。所有参数均可自选是否参与Token生成
     * 
     * @param mUserID
     *            用户ID
     * @param mTime
     *            毫秒时间
     * @param mOS
     *            操作系统信息
     * @param mBrowser
     *            浏览器信息
     * @return Token令牌字符串
     */
    String generateToken(int mUserID, long mTime, String mOS, String mBrowser);

    /**
     * 随机生成一个用于邮箱验证步骤中的认证码，四位字符串
     * 
     * @param mInitSeed
     *            随机种子，可选用
     * @return 随机生成的认证码，为四位长的字符串
     */
    String generateEmailAuthCode(long mInitSeed);

}

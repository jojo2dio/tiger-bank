package org.zoo.common.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 密码加密工具类（基于Hutool，兼容更多版本）
 */
public class PasswordUtil {

    // 盐值，实际项目中建议放在配置文件中
    private static final String SALT = "tiger-bank-salt-2025";

    // 直接创建Digester实例，避免使用可能不存在的SecureUtil.digester()方法
    private static final Digester digester = new Digester(DigestAlgorithm.SHA256);

    /**
     * 加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        // 密码加盐加密：password + salt
        return digester.digestHex(password + SALT);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String rawPassword, String encodedPassword) {
        return encrypt(rawPassword).equals(encodedPassword);
    }
}

package org.example.clothesweb.util;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtil {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

    private static final int BCRYPT_WORK_FACTOR = 12;

    public static String encryptPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_WORK_FACTOR));
        logger.debug("密码加密成功");
        return hashedPassword;
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
            logger.debug("密码验证结果: {}", matches);
            return matches;
        } catch (Exception e) {
            logger.error("密码验证失败", e);
            return false;
        }
    }
}

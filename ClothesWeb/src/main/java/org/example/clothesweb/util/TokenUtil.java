package org.example.clothesweb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();
    private static final long TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    private static class TokenInfo {
        Long userId;
        long createTime;

        TokenInfo(Long userId, long createTime) {
            this.userId = userId;
            this.createTime = createTime;
        }
    }

    public static String generateToken(Long userId) {
        String token = Base64.getEncoder().encodeToString((userId + ":" + System.currentTimeMillis() + ":" + UUID.randomUUID().toString()).getBytes());
        tokenStore.put(token, new TokenInfo(userId, System.currentTimeMillis()));
        logger.debug("Token生成成功: userId={}", userId);
        return token;
    }

    public static Long getUserId(String token) {
        TokenInfo info = tokenStore.get(token);
        return info != null ? info.userId : null;
    }

    public static boolean validateToken(String token) {
        TokenInfo info = tokenStore.get(token);
        if (info == null) {
            logger.debug("Token验证失败: Token不存在");
            return false;
        }
        if (System.currentTimeMillis() - info.createTime > TOKEN_EXPIRE_TIME) {
            tokenStore.remove(token);
            logger.debug("Token验证失败: Token已过期");
            return false;
        }
        return true;
    }

    public static void removeToken(String token) {
        tokenStore.remove(token);
        logger.debug("Token已移除");
    }

    public static void clearExpiredTokens() {
        long now = System.currentTimeMillis();
        tokenStore.entrySet().removeIf(entry -> now - entry.getValue().createTime > TOKEN_EXPIRE_TIME);
        logger.debug("已清理过期Token");
    }
}

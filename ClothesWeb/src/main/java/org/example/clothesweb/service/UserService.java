package org.example.clothesweb.service;

import org.example.clothesweb.dao.UserDAO;
import org.example.clothesweb.entity.User;
import org.example.clothesweb.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserDAO userDAO = new UserDAO();

    public User register(String username, String password, String phone, String address) {
        logger.info("用户注册: username={}", username);
        
        if (userDAO.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (userDAO.findByPhone(phone) != null) {
            throw new RuntimeException("手机号已注册");
        }

        String encryptedPassword = PasswordUtil.encryptPassword(password);
        User user = new User(username, encryptedPassword, phone, 2);
        user.setAddress(address);

        if (userDAO.insert(user)) {
            logger.info("用户注册成功: username={}", username);
            return userDAO.findByUsername(username);
        }
        throw new RuntimeException("注册失败");
    }

    public User login(String username, String password) {
        logger.info("用户登录: username={}", username);
        
        User user = userDAO.findByUsername(username);
        if (user == null) {
            user = userDAO.findByPhone(username);
        }

        if (user == null) {
            logger.warn("用户不存在: username={}", username);
            throw new RuntimeException("用户不存在");
        }
        
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            logger.warn("密码错误: username={}", username);
            throw new RuntimeException("密码错误");
        }

        logger.info("用户登录成功: username={}", username);
        return user;
    }

    public User getUserInfo(Long userId) {
        return userDAO.findById(userId);
    }

    public boolean updateUser(User user) {
        logger.info("用户更新信息: userId={}", user.getId());
        
        User existUser = userDAO.findById(user.getId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        User byUsername = userDAO.findByUsername(user.getUsername());
        if (byUsername != null && !byUsername.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已被使用");
        }

        User byPhone = userDAO.findByPhone(user.getPhone());
        if (byPhone != null && !byPhone.getId().equals(user.getId())) {
            throw new RuntimeException("手机号已被使用");
        }

        if (user.getPassword() != null && !user.getPassword().equals(existUser.getPassword())) {
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword()));
        }

        return userDAO.update(user);
    }
}

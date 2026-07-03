package com.qffz.service;

import com.qffz.dto.*;
import com.qffz.exception.BusinessException;
import com.qffz.mapper.CollectMapper;
import com.qffz.mapper.PostMapper;
import com.qffz.mapper.UserMapper;
import com.qffz.pojo.User;
import com.qffz.utils.JwtUtil;
import com.qffz.utils.PasswordUtil;
import com.qffz.utils.StringUtil;
import com.qffz.utils.UserContext;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CollectMapper collectMapper;

    public UserService(UserMapper userMapper, PostMapper postMapper, CollectMapper collectMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.collectMapper = collectMapper;
    }

    public void register(RegisterRequest request) {
        if (StringUtil.isBlank(request.getUsername()) || StringUtil.isBlank(request.getPassword())) {
            throw new BusinessException("用户名和密码不能为空");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(StringUtil.trim(request.getUsername()));
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setNickname(user.getUsername());
        user.setAvatar("");
        user.setEmail(StringUtil.trim(request.getEmail()));
        user.setRole("USER");
        user.setStatus(1);
        user.setBio("");
        userMapper.insert(user);
    }

    public LoginResponse login(LoginRequest request) {
        if (StringUtil.isBlank(request.getUsername()) || StringUtil.isBlank(request.getPassword())) {
            throw new BusinessException("请输入用户名和密码");
        }
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || !PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        user.setPassword(null);
        String token = JwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user);
    }

    public User info() {
        User user = userMapper.findById(UserContext.userId());
        if (user != null) user.setPassword(null);
        return user;
    }

    public void update(User request) {
        request.setId(UserContext.userId());
        if (!StringUtil.isBlank(request.getPassword())) {
            request.setPassword(PasswordUtil.encode(request.getPassword()));
        } else {
            request.setPassword(null);
        }
        userMapper.update(request);
    }

    public PageResult<PostView> myPosts(int page, int size) {
        int offset = (page - 1) * size;
        Long userId = UserContext.userId();
        return new PageResult<>(postMapper.countByUser(userId), postMapper.listByUser(userId, offset, size));
    }

    public Object myCollects() {
        return collectMapper.listByUser(UserContext.userId());
    }

    public PageResult<User> adminUsers(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        var users = userMapper.list(keyword, offset, size);
        users.forEach(user -> user.setPassword(null));
        return new PageResult<>(userMapper.count(keyword), users);
    }

    public void updateStatus(Long id, Integer status) {
        User target = userMapper.findById(id);
        if (target == null) {
            throw new BusinessException("用户不存在");
        }
        if ("ADMIN".equals(target.getRole()) && status != null && status == 0) {
            throw new BusinessException("不能禁用管理员账号");
        }
        userMapper.updateStatus(id, status);
    }

    public void delete(Long id) {
        User target = userMapper.findById(id);
        if (target == null) {
            throw new BusinessException("用户不存在");
        }
        if ("ADMIN".equals(target.getRole())) {
            throw new BusinessException("不能删除管理员账号");
        }
        userMapper.delete(id);
    }
}

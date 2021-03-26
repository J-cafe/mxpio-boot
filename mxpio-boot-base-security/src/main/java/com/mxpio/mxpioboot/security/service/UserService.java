package com.mxpio.mxpioboot.security.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.mxpio.mxpioboot.security.entity.User;

public interface UserService {

    /**
     * 新增用户
     * @param resources /
     */
    void create(User user);

    /**
     * 编辑用户
     * @param resources /
     */
    void update(User user) throws Exception;

    /**
     * 删除用户
     * @param ids /
     */
    void delete(Set<String> usernames);

    /**
     * 根据用户名查询
     * @param userName /
     * @return /
     */
    User findByName(String username);

    /**
     * 修改密码
     * @param username 用户名
     * @param encryptPassword 密码
     */
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param file 文件
     * @return /
     */
    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    void updateEmail(String username, String email);

    /**
     * 查询全部
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Page<User> queryAll(CriteriaQuery<User> criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<User> queryAll(CriteriaQuery<User> criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<User> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     * @param resources /
     */
    void updateCenter(User resources);
}

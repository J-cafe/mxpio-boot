package com.mxpioframework.security.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.vo.UpatePassVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.User;

public interface UserService {

    /**
     * 新增用户
     * @param user /
     */
	User create(User user);

    /**
     * 编辑用户
     * @param user /
     */
    void update(User user) throws Exception;

    /**
     * 删除用户
     * @param usernames /
     */
    void delete(Set<String> usernames);

    /**
     * 根据用户名查询
     * @param username /
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
    Page<User> queryAll(Criteria criteria, Pageable pageable);

    /**
     * 查询全部不分页
     * @param criteria 条件
     * @return /
     */
    List<User> queryAll(Criteria criteria);

    /**
     * 导出数据
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<User> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     * @param user /
     */
    void updateCenter(User user);
    
    /**

	 * 判断当前登录用户是否为管理员

	 * @return 返回ture，则为管理员，否则不是

	 */
	boolean isAdministrator();

	
	/**

	 * 判断是否为管理员

	 * @return 返回ture，则为管理员，否则不是

	 */
	boolean isAdministrator(String username);

    Result<User> updatePassWithCheck(UpatePassVo upatePassVo);
}

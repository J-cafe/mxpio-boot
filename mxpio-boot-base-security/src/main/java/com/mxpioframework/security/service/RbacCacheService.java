package com.mxpioframework.security.service;

import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.Dept;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RbacCacheService {

    /**
     * 数据资源索引
     * @return 按ID列出DataResource
     */
    Map<String, DataResource> findAllDataResourceByCatch();

    /**
     * 获取所有数据权限
     * @return 查询所有DataResource
     */
    List<DataResource> findAllDataResource();

    /**
     * 获取部门用户关系（缓存）
     * @return 部门用户关系
     */
    public Map<String, Set<String>> getAllDeptCodeGroupByUser();

    /**
     * 获取部门索引（缓存）
     * @return 按照ID获取部门
     */
    public Map<String, Dept> getDeptMap();

    /**
     * 获取部门用户关系,ID索引（缓存）
     * @return 部门用户关系
     */
    public Map<String, Set<String>> getAllDeptIdGroupByUser();

    /**
     * 获取部门用户关系,含上级部门,ID索引（缓存）
     * @return 部门用户关系
     */
    Map<String, Set<String>> getAllDeptIdWithFatherGroupByUser();

    /**
     * 根据用户获取岗位ID
     * @param username 用户名
     * @return 岗位ID
     */
    Set<String> getPostKeyByUser(String username);

    public Map<String, List<DataFilter>> findAllDataFilter();
}

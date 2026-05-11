package com.mxpioframework.security.service.impl;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.*;
import com.mxpioframework.security.service.RbacCacheService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("mxpio.security.rbacCacheService")
public class RbacCacheServiceImpl implements RbacCacheService {

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.DATA_RESOURCE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, DataResource> findAllDataResourceByCatch() {
        List<DataResource> list = JpaUtil.linq(DataResource.class).list();
        return JpaUtil.index(list, "path");
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.DATA_LIST_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public List<DataResource> findAllDataResource() {
        List<DataResource> datas = JpaUtil.linq(DataResource.class).list();
        List<Permission> permissions = JpaUtil.linq(Permission.class).equal("resourceType", ResourceType.DATA).list();
        if (!permissions.isEmpty()) {
            Map<String, DataResource> dataMap = JpaUtil.index(datas);
            for (Permission permission : permissions) {
                DataResource data = dataMap.get(permission.getResourceId());
                if(data == null) {
                    continue;
                }
                List<ConfigAttribute> configAttributes = data.getAttributes();
                if (configAttributes == null) {
                    configAttributes = new ArrayList<>();
                    data.setAttributes(configAttributes);
                }
                configAttributes.add(permission);
            }
        }
        return datas;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.USER_DEPT_CODE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, Set<String>> getAllDeptCodeGroupByUser(){
        List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
        Map<String, Set<String>> result = new HashMap<>();
        userDepts.forEach(userDept -> {
            String username = userDept.getUserId();
            Dept dept = getDeptMap().get(userDept.getDeptId());
            if(dept == null){
                return;
            }
            String deptCode = getDeptMap().get(userDept.getDeptId()).getDeptCode();
            Set<String> deptCodes = result.get(username);
            if(CollectionUtils.isEmpty(deptCodes)){
                deptCodes = new HashSet<>();
            }
            deptCodes.add(deptCode);
            result.put(username, deptCodes);
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.DEPT_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, Dept> getDeptMap() {
        List<Dept> depts = JpaUtil.linq(Dept.class).list();
        return JpaUtil.index(depts);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.DEPT_CODE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, Dept> getDeptMapByCode() {
        List<Dept> depts = JpaUtil.linq(Dept.class).list();
        return JpaUtil.index(depts,"deptCode");
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.USER_DEPT_ID_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, Set<String>> getAllDeptIdGroupByUser() {
        List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
        Map<String, Set<String>> result = new HashMap<>();
        userDepts.forEach(userDept -> {
            String username = userDept.getUserId();
            String deptId = userDept.getDeptId();
            Set<String> deptIds = result.get(username);
            if(CollectionUtils.isEmpty(deptIds)){
                deptIds = new HashSet<>();
            }
            deptIds.add(deptId);
            result.put(username, deptIds);
        });
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.USER_DEPT_ID_FA_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, Set<String>> getAllDeptIdWithFatherGroupByUser() {
        List<UserDept> userDepts = JpaUtil.linq(UserDept.class).list();
        Map<String, Set<String>> result = new HashMap<>();
        for (UserDept userDept : userDepts) {
            String username = userDept.getUserId();
            String deptId = userDept.getDeptId();
            Set<String> deptIds = result.get(username);
            if(CollectionUtils.isEmpty(deptIds)){
                deptIds = new HashSet<>();
            }
            Map<String, Dept> deptMap = getDeptMap();
            addFaDept(deptIds, deptId, deptMap);
            deptIds.add(deptId);
            result.put(username, deptIds);
        }
        return result;
    }

    private void addFaDept(Set<String> deptIds, String deptId, Map<String, Dept> deptMap) {
    	if(deptMap.containsKey(deptId)&&deptMap.get(deptId)!=null) {
    		String faDeptId = deptMap.get(deptId).getFaDeptId();
            if (StringUtils.isNotEmpty(faDeptId)){
                addFaDept(deptIds, faDeptId, deptMap);
                deptIds.add(faDeptId);
            }
    	}
        
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = Constants.USER_POST_CODE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Set<String> getPostKeyByUser(String username) {
        User user = JpaUtil.getOne(User.class, username);
        Set<String> result = new HashSet<>();
        result.add(user.getPostId());
        if(user.getConcurrentPostIds() != null){
            String[] concurrentPostIds = user.getConcurrentPostIds().split(",");
            result.addAll(Arrays.asList(concurrentPostIds));
        }
        return result;
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = Constants.DATA_FILTER_ROLE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
    public Map<String, List<DataFilter>> findAllDataFilter() {
        List<RoleDataFilter> roleDataFilters = JpaUtil.linq(RoleDataFilter.class).list();
        List<DataFilter> dataFilters = JpaUtil.linq(DataFilter.class).list();
        Map<String, DataFilter> dataFilterMap = JpaUtil.index(dataFilters);
        Map<String, List<DataFilter>> result = new HashMap<>();
        roleDataFilters.forEach(roleDataFilter -> {
            if (!result.containsKey(roleDataFilter.getRoleId())) {
                result.put(roleDataFilter.getRoleId(), new ArrayList<DataFilter>() {
                    private static final long serialVersionUID = 1L;
                    {
                        DataFilter dataFilter = new DataFilter();
                        //拷贝对象，解决同一个DataFilter被多个角色引用时，redis缓存存放对象变为对象引用，导致取值时为空的问题
                        //"$ref":"$.68bd82da\\-0575\\-4143\\-b82c\\-1a0fb5bcce2a[1]"
                        BeanReflectionUtils.copyProperties(dataFilter,dataFilterMap.get(roleDataFilter.getDataFilterId()));
                        add(dataFilter);
                    }
                });
            } else {
                DataFilter dataFilter = new DataFilter();
                BeanReflectionUtils.copyProperties(dataFilter,dataFilterMap.get(roleDataFilter.getDataFilterId()));
                result.get(roleDataFilter.getRoleId()).add(dataFilter);
            }
        });
        return result;
    }
}

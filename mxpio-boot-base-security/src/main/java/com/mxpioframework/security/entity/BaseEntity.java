package com.mxpioframework.security.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxpioframework.jpa.MxpioEntity;
import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.jpa.policy.impl.CreatedDatePolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.jpa.policy.impl.UpdatedDatePolicy;
import com.mxpioframework.security.service.policy.CreatorPolicy;
import com.mxpioframework.security.service.policy.ModifierPolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements DictAble, Serializable, MxpioEntity {

  private static final long serialVersionUID = 1L;

  
  @Generator(policy = CreatorPolicy.class)
  @Column(name = "CREATE_BY", updatable = false)
  @Schema(description = "创建人")
  private String createBy;
  
  @Column(name = "CREATE_DEPT", updatable = false)
  @Schema(description = "创建部门")
  private String createDept;

  @Generator(policy = ModifierPolicy.class)
  @Column(name = "UPDATE_BY")
  @Schema(description = "更新人")
  private String updateBy;

  @Generator(policy = CreatedDatePolicy.class)
  @Column(name = "CREATE_TIME", updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @Schema(description = "创建时间")
  private Date createTime;

  @Generator(policy = UpdatedDatePolicy.class)
  @Column(name = "UPDATE_TIME")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @Schema(description = "更新时间")
  private Date updateTime;

  @Transient
  @Schema(description = "脏数据状态")
  private CrudType crudType;

  @Transient
  @Schema(description = "是否处理瞬时属性")
  private boolean saveTransient = true;

  @Transient
  private Map<String, String> textMap;

  public String putText(String key, String value) {
    if (textMap == null) {
      textMap = new HashMap<>();
    }
    return textMap.put(key, value);
  }

}

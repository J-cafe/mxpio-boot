package com.mxpioframework.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.jpa.policy.impl.CreatedDatePolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.jpa.policy.impl.UpdatedDatePolicy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "CREATE_BY", updatable = false)
  @ApiModelProperty(value = "创建人")
  private String createBy;

  @Column(name = "UPDATE_BY")
  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @Generator(policy = CreatedDatePolicy.class)
  @Column(name = "CREATE_TIME", updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @Generator(policy = UpdatedDatePolicy.class)
  @Column(name = "UPDATE_TIME")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @Transient
  @ApiModelProperty(value = "脏数据状态")
  private CrudType crudType;

  @Transient
  @ApiModelProperty(value = "是否处理瞬时属性")
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

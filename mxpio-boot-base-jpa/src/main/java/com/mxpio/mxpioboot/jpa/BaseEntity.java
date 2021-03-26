package com.mxpio.mxpioboot.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mxpio.mxpioboot.jpa.annotation.Generator;
import com.mxpio.mxpioboot.jpa.policy.impl.CreatedDatePolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.UpdatedDatePolicy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "create_by", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @Generator(policy = CreatedDatePolicy.class)
    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @Generator(policy = UpdatedDatePolicy.class)
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}

package com.mxpioframework.security.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Data
@MappedSuperclass
public class VersionalEntity extends BaseEntity{

    /**
     * 乐观锁
     * 引入乐观锁，需要注意:如果存在历史数据，那么该字段不能为空，可以给默认值0，否则历史数据的更新操作会报错
     */
    @Version
    @Column(name = "VERSION_")
    @Schema(description = "乐观锁")
    private Integer version;
}

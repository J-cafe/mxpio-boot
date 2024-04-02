package com.mxpioframework.security.entity;

import com.mxpioframework.jpa.annotation.Generator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "MB_POST_TYPE")
@Schema(description="职系对象")
public class PostType extends BaseEntity {

    @Id
    @Generator
    @Schema(description = "ID")
    @Column(name = "ID_")
    private String id;

    @Schema(description = "编码")
    @Column(name = "CODE_")
    private String code;

    @Schema(description = "名称")
    @Column(name = "NAME_")
    private String mame;

}

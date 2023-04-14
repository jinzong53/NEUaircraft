package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2023-03-31
 */
@Getter
@Setter
@ApiModel(value = "Threaten对象", description = "")
public class Threaten implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("飞机名称")
    private String name;

    @ApiModelProperty("飞机高度")
    private String height;

    @ApiModelProperty("飞机类型")
    private String type;

    @ApiModelProperty("飞机速度")
    private String velocity;

    @ApiModelProperty("飞机角度")
    private String angle;

    @ApiModelProperty("飞机距离")
    private String distance;


}

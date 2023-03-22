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
 * @since 2023-03-21
 */
@Getter
@Setter
@ApiModel(value = "Airplane对象", description = "")
public class Airplane implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("飞机数量")
    private Integer number;

    @ApiModelProperty("飞机类别")
    private String type;

    @ApiModelProperty("检测日期")
    private String creatTime;

    @ApiModelProperty("图片")
    private String img;


}

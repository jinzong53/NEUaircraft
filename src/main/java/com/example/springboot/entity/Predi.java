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
 * @since 2023-04-24
 */
@Getter
@Setter
@ApiModel(value = "Predi对象", description = "")
public class Predi implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("飞机名")
    private String name;

    @ApiModelProperty("飞机地理高度")
    private String geo;

    @ApiModelProperty("飞机纬度")
    private String latitude;

    @ApiModelProperty("飞机经度")
    private String longitude;

    @ApiModelProperty("预测高度")
    private String preda;

    @ApiModelProperty("预测纬度")
    private String predla;

    @ApiModelProperty("预测经度")
    private String predlo;

    @ApiModelProperty("时间戳")
    private String time;


}

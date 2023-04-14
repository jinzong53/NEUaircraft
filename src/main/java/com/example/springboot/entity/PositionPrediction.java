package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-03-29
 */
@Getter
@Setter
@TableName("position_prediction")
@ApiModel(value = "PositionPrediction对象", description = "")
public class PositionPrediction implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("时间")
    private String name;

    @ApiModelProperty("X轴位置")
    private String x;

    @ApiModelProperty("Y轴位置")
    private String y;

    @ApiModelProperty("Z轴位置")
    private String z;

}

package com.example.springboot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2023-04-12
 */
@Getter
@Setter
@ApiModel(value = "Predict对象", description = "")
public class Predict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("飞机名")
    private String name;

    @ApiModelProperty("飞机地理高度")
    private String geo_altitude;

    @ApiModelProperty("飞机纬度")
    private String latitude;

    @ApiModelProperty("飞机经度")
    private String longitude;

    @ApiModelProperty("预测高度")
    private String pred_geo_altitude;

    @ApiModelProperty("预测纬度")
    private String pred_latitude;

    @ApiModelProperty("预测经度")
    private String pred_longitude;

    @ApiModelProperty("时间戳")
    private String time_at_server;


}

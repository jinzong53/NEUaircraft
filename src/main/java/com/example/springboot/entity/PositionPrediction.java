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
    private Integer idi;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("数据1")
    private String timeAtServer;

    @ApiModelProperty("数据2")
    private String aircraft;

    @ApiModelProperty("数据3")
    private String latitude;

    @ApiModelProperty("数据4")
    private String longitude;

    @ApiModelProperty("数据5")
    private String baroAltitude;

    @ApiModelProperty("数据6")
    private String geoAltitude;

    @ApiModelProperty("数据7")
    private String numMeasurements;

    @ApiModelProperty("数据8")
    private String serialA;

    @ApiModelProperty("数据9")
    private String timeAtA;

    @ApiModelProperty("数据10")
    private String rssiA;

    @ApiModelProperty("数据11")
    private String latituteA;

    @ApiModelProperty("数据12")
    private String longituteA;

    @ApiModelProperty("数据13")
    private String heightA;

    @ApiModelProperty("数据14")
    private String serialB;

    @ApiModelProperty("数据15")
    private String timeAtB;

    @ApiModelProperty("数据16")
    private String rssiB;

    @ApiModelProperty("数据17")
    private String latitudeB;

    @ApiModelProperty("数据18")
    private String longituteB;

    @ApiModelProperty("数据19")
    private String heightB;

    @ApiModelProperty("数据20")
    private String serialC;

    @ApiModelProperty("数据21")
    private String timeAtC;

    @ApiModelProperty("数据22")
    private String rssiC;

    @ApiModelProperty("数据23")
    private String latituteC;

    @ApiModelProperty("数据24")
    private String longituteC;

    @ApiModelProperty("数据25")
    private String heightC;
}

package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-03-31
 */
@Getter
@Setter
@TableName("threaten_analyse_copy1")
@ApiModel(value = "ThreatenAnalyseCopy1对象", description = "")
public class ThreatenAnalyseCopy1 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("飞机名")
    private String name;

    @ApiModelProperty("高度威胁值")
    private String height;

    @ApiModelProperty("类型威胁值")
    private String type;

    @ApiModelProperty("速度威胁值")
    private String velocity;

    @ApiModelProperty("角度威胁值")
    private String angle;

    @ApiModelProperty("距离威胁值")
    private String distance;

    @ApiModelProperty("总威胁值")
    private String totalScore;

    @ApiModelProperty("总威胁值")
    private String topsisScore;

    @ApiModelProperty("总威胁值")
    private String grayScore;

}

package com.example.springboot.mapper;

import com.example.springboot.entity.PositionPrediction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-03-29
 */
public interface PositionPredictionMapper extends BaseMapper<PositionPrediction> {

    //清空指定表
    @Update("DELETE FROM position_prediction")
    void deletePP();
}

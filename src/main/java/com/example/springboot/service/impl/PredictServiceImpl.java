package com.example.springboot.service.impl;

import com.example.springboot.entity.Predict;
import com.example.springboot.mapper.PredictMapper;
import com.example.springboot.service.IPredictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-04-12
 */
@Service
public class PredictServiceImpl extends ServiceImpl<PredictMapper, Predict> implements IPredictService {

}

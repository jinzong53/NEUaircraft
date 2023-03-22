package com.example.springboot.service.impl;

import com.example.springboot.entity.Airplane;
import com.example.springboot.mapper.AirplaneMapper;
import com.example.springboot.service.IAirplaneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2023-03-21
 */
@Service
public class AirplaneServiceImpl extends ServiceImpl<AirplaneMapper, Airplane> implements IAirplaneService {

}

package io.renren.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.DaPointsQryEntity;

/**
 * 
 *
 * @author	wangjh 
 * @date 2018-11-18 09:20:47
 */
public interface DaPointsQryService extends IService<DaPointsQryEntity> {

	PageUtils queryPage(Map<String, Object> params);
    
    List<DaPointsQryEntity> queryDaPointsBySql(Map<String, Object> data);

	int getDaPointsCount(Map<String, Object> data);
}


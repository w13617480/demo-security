package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.DaPointsEntity;
import io.renren.modules.sys.entity.DaPointsQryEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-14 09:20:47
 */
public interface DaPointsQryDao extends BaseMapper<DaPointsQryEntity> {

	List<DaPointsQryEntity> queryDaPointsBySql(Map<String,Object> data);
	int getDaPointsCount(Map<String, Object> data);
}

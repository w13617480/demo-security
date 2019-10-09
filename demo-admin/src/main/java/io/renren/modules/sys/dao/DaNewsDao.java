package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.DaNewsEntity;
import io.renren.modules.sys.entity.DaPointsQryEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-18 16:30:59
 */
public interface DaNewsDao extends BaseMapper<DaNewsEntity> {
	List<DaNewsEntity> queryDaNewsBySql(Map<String,Object> data);
	int getDaNewsCount(Map<String, Object> data);
}

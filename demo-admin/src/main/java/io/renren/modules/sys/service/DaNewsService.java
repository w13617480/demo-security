package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.DaNewsEntity;
import io.renren.modules.sys.entity.DaPointsQryEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-18 16:30:59
 */
public interface DaNewsService extends IService<DaNewsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<DaNewsEntity> queryDaNewsBySql(Map<String, Object> data);

	int getDaNewsCount(Map<String, Object> data);
}


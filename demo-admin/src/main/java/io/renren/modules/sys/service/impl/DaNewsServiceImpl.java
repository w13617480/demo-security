package io.renren.modules.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.dao.DaNewsDao;
import io.renren.modules.sys.entity.DaNewsEntity;
import io.renren.modules.sys.entity.DaPointsQryEntity;
import io.renren.modules.sys.service.DaNewsService;


@Service("daNewsService")
public class DaNewsServiceImpl extends ServiceImpl<DaNewsDao, DaNewsEntity> implements DaNewsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        Page<DaNewsEntity> page = this.selectPage(
//                new Query<DaNewsEntity>(params).getPage(),
//                new EntityWrapper<DaNewsEntity>()
//        );
//
//        return new PageUtils(page);
    	
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        String sidx = params.get("sidx").toString();
        String order = params.get("order").toString();
    	String channel1 = (String)params.get("channel1");
    	String channel2 = (String)params.get("channel2");
    	
        Map<String, Object> data = new HashedMap();
        data.put("currIndex", (currPage-1)*pageSize);
        data.put("pageSize", pageSize);
        data.put("sidx", sidx);
        data.put("order", order);
        data.put("channel1", channel1);
        data.put("channel2", channel2);
        
        List<DaNewsEntity> daPointsList = this.queryDaNewsBySql(data);
        int totalCount = this.getDaNewsCount(data);

        PageUtils x = new PageUtils(daPointsList, totalCount, pageSize, currPage);

        return  x;
    }
    
    @Override
	public List<DaNewsEntity> queryDaNewsBySql(Map<String, Object> data) {
        List<DaNewsEntity> x =  baseMapper.queryDaNewsBySql(data);
        return x;
	}

	@Override
	public int getDaNewsCount(Map<String, Object> data) {
		
        int x =  baseMapper.getDaNewsCount(data);
        
        return x;
	}

}

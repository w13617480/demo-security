package io.renren.modules.sys.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.DaPointsQryDao;
import io.renren.modules.sys.entity.DaPointsQryEntity;
import io.renren.modules.sys.service.DaPointsQryService;


@Service("daPointsQryService")
public class DaPointsQryServiceImpl extends ServiceImpl<DaPointsQryDao, DaPointsQryEntity> implements DaPointsQryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	/*
        Page<DaPointsQryEntity> page = this.selectPage(
                new Query<DaPointsQryEntity>(params).getPage(),
                new EntityWrapper<DaPointsQryEntity>()
        );

        return new PageUtils(page);
        */
    	
    	String name = (String)params.get("name");
    	String start = (String)params.get("start");
   	 	String ends = (String)params.get("ends");
   	 	
   	 	if ( name == null ) {
   	 		name = "";
   	 	}
   	 	
   	 	if ( start == null ) {
   	 		start = "";
   	 	}
   	 	
   	 	if ( ends == null ) {
   	 		ends = "";
   	 	}
   	 	
        
        System.out.println("page:" + params.get("page"));
        System.out.println("limit:" + params.get("limit"));
        System.out.println("sidx:" + params.get("sidx"));
        System.out.println("order:" + params.get("order"));
        
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        String sidx = params.get("sidx").toString();
        String order = params.get("order").toString();
        
        
        Map<String, Object> data = new HashedMap();
        data.put("currIndex", (currPage-1)*pageSize);
        data.put("pageSize", pageSize);
        data.put("sidx", sidx);
        data.put("order", order);
        data.put("name", name);
        data.put("start", start);
        data.put("ends", ends);
        
        List<DaPointsQryEntity> daPointsList = this.queryDaPointsBySql(data);
        int totalCount = this.getDaPointsCount(data);

        PageUtils x = new PageUtils(daPointsList, totalCount, pageSize, currPage);

        return  x;//new PageUtils(page);

    }
    
    @Override
	public List<DaPointsQryEntity> queryDaPointsBySql(Map<String, Object> data) {
        List<DaPointsQryEntity> x =  baseMapper.queryDaPointsBySql(data);
        return x;
	}

	@Override
	public int getDaPointsCount(Map<String, Object> data) {
		
        
        int x =  baseMapper.getDaPointsCount(data);
        
        return x;
	}


}

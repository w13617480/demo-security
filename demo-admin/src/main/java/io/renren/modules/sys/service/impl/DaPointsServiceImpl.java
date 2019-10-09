package io.renren.modules.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.DaPointsDao;
import io.renren.modules.sys.entity.DaPointsEntity;
import io.renren.modules.sys.service.DaPointsService;


@Service("daPointsService")
public class DaPointsServiceImpl extends ServiceImpl<DaPointsDao, DaPointsEntity> implements DaPointsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	/*
        Page<DaPointsEntity> page = this.selectPage(
                new Query<DaPointsEntity>(params).getPage(),
                new EntityWrapper<DaPointsEntity>()
               
        );
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
   	 	
//   	 	if (name.equals("")) {
//   	 		name = null;
//   	 	}
//   	 	
//   	 	if ( start.equals("")) {
//   	 		start = null;
//   	 	}
//   	 	
//   	 	if ( ends.equals("")) {
//   	 		ends = null;
//   	 	}
        
        System.out.println("page:" + params.get("page"));
        System.out.println("limit:" + params.get("limit"));
        System.out.println("sidx:" + params.get("sidx"));
        System.out.println("order:" + params.get("order"));
        //sidx=userid, order=desc
        
        int currPage = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());
        String sidx = params.get("sidx").toString();
        String order = params.get("order").toString();
        
        int totalCount = this.getCount();
        
        Map<String, Object> data = new HashedMap();
        data.put("currIndex", (currPage-1)*pageSize);
        data.put("pageSize", pageSize);
        data.put("sidx", sidx);
        data.put("order", order);
        data.put("name", name);
        data.put("start", start);
        data.put("ends", ends);
        
        List<DaPointsEntity> daPointsList = this.queryStudentsBySql(data);

        PageUtils x = new PageUtils(daPointsList, totalCount, pageSize, currPage);
        //PageUtils x = new PageUtils(  page, daPointsList);

        return  x;//new PageUtils(page);
    }

	@Override
	public List<DaPointsEntity> queryStudentsBySql(Map<String, Object> data) {
        
        List<DaPointsEntity> x =  baseMapper.queryStudentsBySql(data);
        
        return x;
	}
	
	@Override
	public int getCount() {
		
        
        int x =  baseMapper.getCount();
        
        return x;
	}


}

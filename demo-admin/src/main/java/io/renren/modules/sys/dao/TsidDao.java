package io.renren.modules.sys.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.sys.entity.TsidEntity;

public interface TsidDao extends BaseMapper<TsidEntity>{
	TsidEntity insterTsid();
	 void deletctTsid(int parentId);
}

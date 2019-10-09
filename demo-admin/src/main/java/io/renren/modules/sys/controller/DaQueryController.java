package io.renren.modules.sys.controller;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.service.DaNewsService;
import io.renren.modules.sys.service.DaPointsQryService;

@RestController
@RequestMapping("sys/daquery")
public class DaQueryController {
	@Autowired
    private DaPointsQryService daPointsQryService;
	@Autowired
	private DaNewsService daNewsService;
	
	
	@RequestMapping("/querypoints")
    @RequiresPermissions("sys:daquery:querypoints")
    public R queryPoints(@RequestParam Map<String, Object> params){
        PageUtils page = daPointsQryService.queryPage(params);

        return R.ok().put("page", page);
    }
	
	@RequestMapping(value = "/querynews", method = RequestMethod.POST)
    //@RequiresPermissions("sys:daquery:querynews")
    public R queryNews(@RequestParam Map<String, Object> params){
        PageUtils page = daNewsService.queryPage(params);
        return R.ok().put("page", page);
    }

}

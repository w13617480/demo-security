package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.group.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.DaPointsEntity;
import io.renren.modules.sys.entity.DaUserEntity;
import io.renren.modules.sys.service.DaPointsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-13 14:29:17
 */
@RestController
@RequestMapping("sys/dapoints")
public class DaPointsController {
    @Autowired
    private DaPointsService daPointsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dapoints:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = daPointsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dapoints:info")
    public R info(@PathVariable("id") Long id){
        DaPointsEntity daPoints = daPointsService.selectById(id);

        return R.ok().put("daPoints", daPoints);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dapoints:save")
    public R save(@RequestBody DaPointsEntity daPoints){
       
       if(ValidatorUtils.validateEntity(daPoints)==null){
           daPointsService.insert(daPoints);
           return R.ok();
       }else{
         return ValidatorUtils.validateEntity(daPoints);
       }
        
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dapoints:update")
    public R update(@RequestBody DaPointsEntity daPoints){
       if(ValidatorUtils.validateEntity(daPoints)==null){
          daPointsService.updateAllColumnById(daPoints);//全部更新
          return R.ok();
        }else{
          return ValidatorUtils.validateEntity(daPoints);
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dapoints:delete")
    public R delete(@RequestBody Long[] ids){
        daPointsService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }
    
//    @RequestMapping("/queryStudentsBySql")
//    @RequiresPermissions("sys:dapoints:queryStudentsBySql")
//    public List<DaPointsEntity> queryStudentsBySql(int currPage, int pageSize){
//        
//        List<DaPointsEntity> daPointsList = daPointsService.queryStudentsBySql(currPage, pageSize);
//        return daPointsList;
//    }
}

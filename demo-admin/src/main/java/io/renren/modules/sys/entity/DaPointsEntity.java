package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-13 14:29:17
 */
@TableName("da_points")
public class DaPointsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@NotBlank(message="不能为空")
private Long id;
	/**
	 * 
	 */
	@NotBlank(message="不能为空")
private Long userid;
	/**
	 * 发生时间
	 */
	@NotBlank(message="发生时间不能为空")
private Date occtime;
	/**
	 * 积分类型
	 */
	@NotBlank(message="积分类型不能为空")
private Integer type;
	/**
	 * 积分数
	 */
	@NotBlank(message="积分数不能为空")
private Integer points;
	/**
	 * 备注
	 */
	@NotBlank(message="备注不能为空")
private String memo;
	
	private String name;
	private String dept_name;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	/**
	 * 获取：
	 */
	public Long getUserid() {
		return userid;
	}
	/**
	 * 设置：发生时间
	 */
	public void setOcctime(Date occtime) {
		this.occtime = occtime;
	}
	/**
	 * 获取：发生时间
	 */
	public Date getOcctime() {
		return occtime;
	}
	/**
	 * 设置：积分类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：积分类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：积分数
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	/**
	 * 获取：积分数
	 */
	public Integer getPoints() {
		return points;
	}
	/**
	 * 设置：备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * 获取：备注
	 */
	public String getMemo() {
		return memo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	
	
}

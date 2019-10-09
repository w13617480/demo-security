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
 * @date 2018-11-18 16:30:59
 */
@TableName("da_news")
public class DaNewsEntity implements Serializable {
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
private String title;
	/**
	 * 操作时间
	 */
	@NotBlank(message="操作时间不能为空")
private Date optime;
	
private String opdate;
	
	
	/**
	 * 操作人
	 */
	@NotBlank(message="操作人不能为空")
private Integer operator;
	/**
	 * 点赞数
	 */
	@NotBlank(message="点赞数不能为空")
private Integer likes;

private Integer comments;

	/**
	 * 
	 */
	@NotBlank(message="不能为空")
private String content;
	/**
	 * 版块
	 */
	@NotBlank(message="版块不能为空")
private Integer channel1;

private Integer channel2;

	/**
	 * 首页轮播图
	 */
	@NotBlank(message="首页轮播图不能为空")
private String banner;
	/**
	 * 作者
	 */
	@NotBlank(message="作者不能为空")
private Integer author;

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
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：操作时间
	 */
	public void setOptime(Date optime) {
		this.optime = optime;
	}
	/**
	 * 获取：操作时间
	 */
	public Date getOptime() {
		return optime;
	}
	/**
	 * 设置：操作人
	 */
	public void setOperator(Integer operator) {
		this.operator = operator;
	}
	/**
	 * 获取：操作人
	 */
	public Integer getOperator() {
		return operator;
	}
	/**
	 * 设置：点赞数
	 */
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	/**
	 * 获取：点赞数
	 */
	public Integer getLikes() {
		return likes;
	}
	
	

	
	public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：版块
	 */
	
	public Integer getChannel1() {
		return channel1;
	}
	public void setChannel1(Integer channel1) {
		this.channel1 = channel1;
	}
	
	public Integer getChannel2() {
		return channel2;
	}
	
	public void setChannel2(Integer channel2) {
		this.channel2 = channel2;
	}
	/**
	 * 设置：首页轮播图
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}
	/**
	 * 获取：首页轮播图
	 */
	public String getBanner() {
		return banner;
	}
	/**
	 * 设置：作者
	 */
	public void setAuthor(Integer author) {
		this.author = author;
	}
	/**
	 * 获取：作者
	 */
	public Integer getAuthor() {
		return author;
	}
	public String getOpdate() {
		return opdate;
	}
	public void setOpdate(String opdate) {
		this.opdate = opdate;
	}
	
	
	
}

package com.gblog.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gblog.common.code.Result;
import com.gblog.po.Blog;
import com.gblog.service.BlogService;
import com.gblog.utils.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-03-23
 */
@RestController
//@RequestMapping("/blog")
public class BlogController {

	@Autowired
	private BlogService blogService;

	@GetMapping("/blogs")
	public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
		Page page = new Page(currentPage, 5);
		IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

		return Result.succ(pageData);
	}


	@GetMapping("/blog/{id}")
	public Result detail(@PathVariable(name = "id") Long id) {
		Blog blog = blogService.getById(id);
		Assert.notNull(blog, "该博客已被删除");

		return Result.succ(blog);
	}


	/**
	 * 编辑博客
	 * @RequiresAuthentication 该注解表示需要登录才能访问
	 * @param blog
	 * @return
	 */
	@RequiresAuthentication
	@PostMapping("/blog/edit")
	public Result edit(@Validated @RequestBody Blog blog) {

//        Assert.isTrue(false, "公开版不能任意编辑！");

		Blog temp = null;
		if(blog.getId() != null) {
			temp = blogService.getById(blog.getId());
			// 只能编辑自己的文章
			System.out.println(ShiroUtil.getProfile().getId());
			Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

		} else {

			temp = new Blog();
			temp.setUserId(ShiroUtil.getProfile().getId());
			temp.setCreated(LocalDateTime.now());
			temp.setStatus(0);
		}

		BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
		blogService.saveOrUpdate(temp);

		return Result.succ(null);
	}

}


package com.demo.util;



/**
 * 用来分页的类
 * @author Administrator
 *
 */
public class Page 
{
	private int currentPage;
	private int pageSize = 10;
	private int totalPage ;//一共多少页
	private int totalCount;//表里一共多少条数据
	public int getCurrentPage() 
	{//把对当前页的判断写在这里
		if(currentPage <=0)
		{
			currentPage = 1;
		}else if(currentPage > getTotalPage())
		{
			currentPage = getTotalPage();
		}
		return currentPage;
	}
	public void setCurrentPage(int currentPage) 
	{
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		totalPage = 
		totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize + 1;
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	

}

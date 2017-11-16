package com.demo.dao;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface GenericDao {
	public void  insert(Entity e) ; 
	//添加
	public void  update(Entity e);
	//更新
	public void  delect(Entity e) ; 
	//删除
	public List<Entity> selectAll(Entity e)  ;
	//查询，返回集合
	public Entity selectById(Entity e) ;
	//查询，单个返回 
	public List<Entity> selectLike(Entity e) ;
	//查询，返回集合 like
	public List<Entity> selectOrderBy(Entity e);
	//查询，返回集合 
	public List<Entity> selectLimit(Entity e,int pageSize,int totalCount);
	//查询，返回集合 
	public List<Entity> selectBetweenAnd(Entity e);
	//查询，返回集合 
    public List<Entity> selectCount(Entity e);
	
	

}

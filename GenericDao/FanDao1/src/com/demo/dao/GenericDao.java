package com.demo.dao;


import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface GenericDao {
	public void  insert(Entity e) ; 
	//���
	public void  update(Entity e);
	//����
	public void  delect(Entity e) ; 
	//ɾ��
	public List<Entity> selectAll(Entity e)  ;
	//��ѯ�����ؼ���
	public Entity selectById(Entity e) ;
	//��ѯ���������� 
	public List<Entity> selectLike(Entity e) ;
	//��ѯ�����ؼ��� like
	public List<Entity> selectOrderBy(Entity e);
	//��ѯ�����ؼ��� 
	public List<Entity> selectLimit(Entity e,int pageSize,int totalCount);
	//��ѯ�����ؼ��� 
	public List<Entity> selectBetweenAnd(Entity e);
	//��ѯ�����ؼ��� 
    public List<Entity> selectCount(Entity e);
	
	

}

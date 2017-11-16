package com.demo.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.demo.util.ConnectionManager;

public class Dao implements Entity,GenericDao
{
	private String SQL_INSERT="insert";
	private String SQL_UPDATE="update";
	private String SQL_DELECT="delect";
	private String SQL_SELECT="select";
	private Class  clazz;//类
    private String tableName;//表名
    private Method[] methods;//方法名
    private Field[] field;//属性
    private Connection conn;
    private String sql;
    //增加
	@Override
	public void insert(Entity e)  {
	   //(获取类)判断是哪个类使用insert方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//设置字段
		String fields = "";
		//设置数值
		String value="";
		//中间代替物
		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
	      Object o = "";
	    //SQL语句
	    //根据获取的属性数量进行循环，通过切割再拼接的方法完成SQL语句
	      for(int i = 0;i<field.length;i++)
	      {
	    	  if(!field[i].getName().equalsIgnoreCase("id"))
	    	  {
	    		  //insert  into  表名 （字段） VALUES （数值）
	    		  fields = fields+","+field[i].getName();
	    		  value = "get"+field[i].getName().substring(0, 1).toUpperCase()+field[i].getName().substring(1);
	    		  try {
					try {
						o = o+",'"+clazz.getMethod(value).invoke(e)+"'";
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		  
	    	  }
	      }
	      //去逗号
	      fields = fields.substring(1);
	      value = (String) o;
	      value = value.substring(1);
	      conn = ConnectionManager.getConnection();
	      PreparedStatement ps  =  null;
	      sql = SQL_INSERT + "into" + tableName+"("+fields+")"+"values("+value+")";
	      try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			//释放资源  release()方法在ConnectionManager
			ConnectionManager.release(ps,null);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      
	}
	//修改   更新
	@Override
	public void update(Entity e)  {
		//假如通过id查询修改
		//update table set 字段 = 字段 where 字段=条件；
		//(获取类)判断是哪个类使用update方法
				clazz = e.getClass();
			   //(获取表名)先获取类名，然后将类名转化为小写的表名
				tableName = clazz.getSimpleName().toLowerCase();
				//获取实体类方法
				methods  = clazz.getDeclaredMethods();
				//获取对应的属性
				field = clazz.getDeclaredFields();
				//获取实体类的对象
				try {
					Object objectInstance = clazz.newInstance();
				} catch (InstantiationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IllegalAccessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//设置字段
				String fields = "";
				//设置数值
				String value="";
				//中间代替物
				//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
			      Object o = "";
			      conn = ConnectionManager.getConnection();
			      PreparedStatement ps  =  null;
			      //获取需要修改的id
			      try {
					Object id = null;
					try {
						id = clazz.getMethod("getId").invoke(e);
					} catch (IllegalAccessException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					for(int i = 1;i<field.length;i++)
					{
						if(!field[i].getName().equalsIgnoreCase("id"));
						sql = SQL_UPDATE+" "+tableName+"set"+field[i].getName()+" = ? "+"where id ="+id;
	                    try {
							ps = conn.prepareStatement(sql);
							fields = "get"+field[i].getName().substring(0,1).toUpperCase()+field[i].getName().substring(1);
			                    try {
									o = clazz.getMethod(fields).invoke(e);
								} catch (IllegalAccessException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
			                    //判断字段是否存在，存在则修改，不存在则不修改
			                    if(o!=null){
			                        ps.setObject(1, o);
			                        ps.executeUpdate();
			                    }
			                    else
			                        continue;
			                    ConnectionManager.release(ps, null);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
	//删除
	@Override
	public void delect(Entity e)  {
	     //delete from 表名 where id = ?;
		//(获取类)判断是哪个类使用delect方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//设置字段
		String fields = "";
		//设置数值
		String value="";
		//中间代替物
		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
	      Object o = "";
	      conn = ConnectionManager.getConnection();
	      PreparedStatement ps  =  null;
	      //获取需要删除的id
	      try {
			
			for(int i = 1;i<field.length;i++)
			{
				if(!field[i].getName().equals("id"));
				sql = SQL_DELECT+" from "+tableName+" where id = ? ";
                try {
					ps = conn.prepareStatement(sql);
					fields = "get"+field[i].getName().substring(0,1).toUpperCase()+field[i].getName().substring(1);
	                    try {
							o = clazz.getMethod(fields).invoke(e);
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                    int id = (int) o;
	                    ps.setInt(1, id);
	                    ps.execute();
	                    ConnectionManager.release(ps, null);
            	} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//查询   select*from 表名
	@Override
	public List<Entity> selectAll(Entity e)  {
		 //(获取类)判断是哪个类使用selectAll方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalAccessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        List<Entity> list = new ArrayList<Entity>();
        sql="select * from "+tableName;
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		return list;
	}
	//查询   select*from 表名 where id
	@Override
	public Entity selectById(Entity e)  {
		 //(获取类)判断是哪个类使用selectById方法
			clazz = e.getClass();
		   //(获取表名)先获取类名，然后将类名转化为小写的表名
			tableName = clazz.getSimpleName().toLowerCase();
			//获取实体类方法
			methods  = clazz.getDeclaredMethods();
			//获取对应的属性
			field = clazz.getDeclaredFields();
			//获取实体类的对象
			try {
				Object objectInstance = clazz.newInstance();
			} catch (InstantiationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IllegalAccessException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 sql = SQL_SELECT+" * from "+tableName+" where id = ?";
	            conn = ConnectionManager.getConnection();
	            PreparedStatement ps=null;
	            
	            try {
					 ps = conn.prepareStatement(sql);
					 //获取用户的id封装进SQL语句中
			            try {
							try {
								ps.setObject(1, clazz.getMethod("getId").invoke(e));
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (NoSuchMethodException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SecurityException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			            ResultSet rs = ps.executeQuery();
			            //获取数据库元数据
			            ResultSetMetaData rsmd = rs.getMetaData();
			            List<String> columnList = new ArrayList<String>();
			            //通过列的数量遍历表对应的列名，保存在集合中
			            for(int i = 0;i<rsmd.getColumnCount();i++){
			        columnList.add(rsmd.getColumnName(i+1));
			            }
			            while(rs.next()){
			                for(int i = 0;i<columnList.size();i++){
			                    //获取集合里的列名
			                    String column = columnList.get(i);
			                    //根据列名切割生成set方法
			                    String setMethod = "set"+column.substring(0, 1).toUpperCase()+column.substring(1);
			                    //获取数组中的方法名
			                    for(int j=0;j <methods.length;j++){
			                        //取得method对象
			                        Method m = methods[j];
			                        //判断反射获取所有的方法中是否有与根据列名切割成的set方法相同
			                if(m.getName().equals(setMethod)){
			                            //调用set方法封装数据
			                            try {
											try {
												m.invoke(e, rs.getObject(column));
											} catch (IllegalAccessException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										} catch (IllegalArgumentException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (InvocationTargetException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}//根据列名获取结果接中对应的值
			                        }
			                    }
			                }
			            }
			            ConnectionManager.release(ps, rs);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		return e;
	}
	@Override
	public List<Entity> selectLike(Entity e)  {
		 //(获取类)判断是哪个类使用selectLike方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalAccessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        List<Entity> list = new ArrayList<Entity>();
            //设置字段
      		String fields = "";
      		//设置数值
      		String value="";
      		//中间代替物
      		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
      	      Object o = "";
      	    for(int i = 0;i<field.length;i++)
  	      {
  	    	  if(!field[i].getName().equalsIgnoreCase("id"))
  	    	  {
  	    		  //insert  into  表名 （字段） VALUES （数值）
  	    		  fields = fields+","+field[i].getName();
  	    		  value = "get"+field[i].getName().substring(0, 1).toUpperCase()+field[i].getName().substring(1);
  	    		  try {
  					try {
						o = o+",'"+clazz.getMethod(value).invoke(e)+"'";
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  				} catch (IllegalArgumentException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (InvocationTargetException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (NoSuchMethodException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (SecurityException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
  	    		  
  	    	  }
  	      }
  	      //去逗号
  	      fields = fields.substring(1);
  	      value = (String) o;
  	      value = value.substring(1);
  	    
//        SELECT * FROM tb_stu WHERE sname = '小刘'
//        SELECT * FROM tb_stu WHERE sname like '刘%'
//        SELECT * FROM tb_stu WHERE sname like '%程序员'
//        SELECT * FROM tb_stu WHERE sname like '%PHP%'
//	      sql = SQL_INSERT + "into" + tableName+"("+fields+")"+"values("+value+")";

        sql="select * from "+tableName+" where"+"("+fields+")"+" like ("+value+")";
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		return list;

	}
	@Override
	public List<Entity> selectOrderBy(Entity e) {

		 //(获取类)判断是哪个类使用selectOrderBy方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} catch (IllegalAccessException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
        List<Entity> list = new ArrayList<Entity>();
            //设置字段
      		String fields = "";
      		//设置数值
      		String value="";
      		//中间代替物
      		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
      	      Object o = "";
      	    for(int i = 0;i<field.length;i++)
  	      {
  	    	  if(!field[i].getName().equalsIgnoreCase("id"))
  	    	  {
  	    		 
  	    		  fields = fields+","+field[i].getName();
  	    		  value = "get"+field[i].getName().substring(0, 1).toUpperCase()+field[i].getName().substring(1);
  	    		  try {
  					try {
						o = o+",'"+clazz.getMethod(value).invoke(e)+"'";
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  				} catch (IllegalArgumentException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (InvocationTargetException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (NoSuchMethodException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (SecurityException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
  	    		  
  	    	  }
  	      }
  	      //去逗号
  	      fields = fields.substring(1);
  	      value = (String) o;
  	      value = value.substring(1);
  	    
//  		SELECT Company, OrderNumber FROM Orders ORDER BY Company
//	      sql = SQL_INSERT + "into" + tableName+"("+fields+")"+"values("+value+")";

        sql=SQL_SELECT+" ("+fields+") "+" from "+tableName+" order by ("+value+")";
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		return list;

		
	}
	@Override
	public List<Entity> selectLimit(Entity e,int pageSize,int totalCount) {
		

		 //(获取类)判断是哪个类使用selectLimit方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} catch (IllegalAccessException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
       List<Entity> list = new ArrayList<Entity>();
           //设置字段
     		String fields = "";
     		//设置数值
     		String value="";
     		//中间代替物
     		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
     	      Object o = "";
     	    for(int i = 0;i<field.length;i++)
 	      {
 	    	  if(!field[i].getName().equalsIgnoreCase("id"))
 	    	  {
 	    		 
 	    		  fields = fields+","+field[i].getName();
 	    		  value = "get"+field[i].getName().substring(0, 1).toUpperCase()+field[i].getName().substring(1);
 	    		  try {
 					try {
						o = o+",'"+clazz.getMethod(value).invoke(e)+"'";
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
 				} catch (IllegalArgumentException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} catch (InvocationTargetException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} catch (NoSuchMethodException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				} catch (SecurityException e1) {
 					// TODO Auto-generated catch block
 					e1.printStackTrace();
 				}
 	    		  
 	    	  }
 	      }
 	      //去逗号
 	      fields = fields.substring(1);
 	      value = (String) o;
 	      value = value.substring(1);
 	    
// 		SELECT * FROM table  LIMIT [offset,] rows | rows OFFSET offset


       sql="select * from "+tableName+" limit "+"pageSize,totalCount";
       conn = ConnectionManager.getConnection();
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
		return list;
	}
	@Override
	public List<Entity> selectBetweenAnd(Entity e) {

		 //(获取类)判断是哪个类使用selectBetweenAnd方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} catch (IllegalAccessException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
        List<Entity> list = new ArrayList<Entity>();
            //设置字段
      		String fields = "";
      		//设置数值
      		String value="";
      		//中间代替物
      		//(在遍历的时候循环赋值invoke返回值强转为String的时候会数据丢失)
      	      Object o = "";
      	    for(int i = 0;i<field.length;i++)
  	      {
  	    	  if(!field[i].getName().equalsIgnoreCase("id"))
  	    	  {
  	    		 
  	    		  fields = fields+","+field[i].getName();
  	    		  value = "get"+field[i].getName().substring(0, 1).toUpperCase()+field[i].getName().substring(1);
  	    		  try {
  					try {
						o = o+",'"+clazz.getMethod(value).invoke(e)+"'";
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
  				} catch (IllegalArgumentException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (InvocationTargetException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (NoSuchMethodException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} catch (SecurityException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				}
  	    		  
  	    	  }
  	      }
  	      //去逗号
  	      fields = fields.substring(1);
  	      value = (String) o;
  	      value = value.substring(1);
  	    
//  		SELECT "栏位名" 
//  		FROM " 表格名" 
//  		WHERE "栏位名" BETWEEN '值一' AND '值二';


        sql=SQL_SELECT+" ("+fields+") "+" from "+tableName+" where "+" ("+fields+") "+ " between " +"("+value+")"+" and "+"("+value+")";
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		return list;
	}
	

	@Override
	public List<Entity> selectCount(Entity e) {

		 //(获取类)判断是哪个类使用selectCount方法
		clazz = e.getClass();
	   //(获取表名)先获取类名，然后将类名转化为小写的表名
		tableName = clazz.getSimpleName().toLowerCase();
		//获取实体类方法
		methods  = clazz.getDeclaredMethods();
		//获取对应的属性
		field = clazz.getDeclaredFields();
		//获取实体类的对象
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IllegalAccessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        List<Entity> list = new ArrayList<Entity>();
//		SELECT COUNT(*) FROM table_name
        sql="select count from "+tableName;
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //获取数据库的元数据
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //遍历保存数据库中对应表的列名
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //循环遍历记录
	            while(rs.next()){
	                //创建封装记录的对象
	            	Serializable e1 = null;
					try {
						e1 = (Serializable) clazz.newInstance();
					} catch (InstantiationException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					} catch (IllegalAccessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
	                // 遍历一个记录中的所有列
	                for(int i = 0;i<columnList.size();i++){
	                     // 获取列名
	                    String column = columnList.get(i);
	                    // 根据列名创建set方法
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // 循环遍历methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // 获取每一个method对象
	                    Method m = methods[j];
	                    // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
	                if(m.getName().equals(setMethd)){
	                            // 反调set方法封装数据
	                            try {
									try {
										m.invoke(e1, rs.getObject(column));
									} catch (IllegalAccessException e2) {
										// TODO Auto-generated catch block
										e2.printStackTrace();
									}
								} catch (IllegalArgumentException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								} catch (InvocationTargetException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}// 获取rs中对应的值，封装到obj中
	                    }
	                    }
	                }
	                list.add((Entity) e1);
	            }
	            ConnectionManager.release(ps, rs);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
		return list;
		
	}
}

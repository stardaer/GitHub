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
	private Class  clazz;//��
    private String tableName;//����
    private Method[] methods;//������
    private Field[] field;//����
    private Connection conn;
    private String sql;
    //����
	@Override
	public void insert(Entity e)  {
	   //(��ȡ��)�ж����ĸ���ʹ��insert����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//�����ֶ�
		String fields = "";
		//������ֵ
		String value="";
		//�м������
		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
	      Object o = "";
	    //SQL���
	    //���ݻ�ȡ��������������ѭ����ͨ���и���ƴ�ӵķ������SQL���
	      for(int i = 0;i<field.length;i++)
	      {
	    	  if(!field[i].getName().equalsIgnoreCase("id"))
	    	  {
	    		  //insert  into  ���� ���ֶΣ� VALUES ����ֵ��
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
	      //ȥ����
	      fields = fields.substring(1);
	      value = (String) o;
	      value = value.substring(1);
	      conn = ConnectionManager.getConnection();
	      PreparedStatement ps  =  null;
	      sql = SQL_INSERT + "into" + tableName+"("+fields+")"+"values("+value+")";
	      try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			//�ͷ���Դ  release()������ConnectionManager
			ConnectionManager.release(ps,null);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      
	}
	//�޸�   ����
	@Override
	public void update(Entity e)  {
		//����ͨ��id��ѯ�޸�
		//update table set �ֶ� = �ֶ� where �ֶ�=������
		//(��ȡ��)�ж����ĸ���ʹ��update����
				clazz = e.getClass();
			   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
				tableName = clazz.getSimpleName().toLowerCase();
				//��ȡʵ���෽��
				methods  = clazz.getDeclaredMethods();
				//��ȡ��Ӧ������
				field = clazz.getDeclaredFields();
				//��ȡʵ����Ķ���
				try {
					Object objectInstance = clazz.newInstance();
				} catch (InstantiationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IllegalAccessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//�����ֶ�
				String fields = "";
				//������ֵ
				String value="";
				//�м������
				//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
			      Object o = "";
			      conn = ConnectionManager.getConnection();
			      PreparedStatement ps  =  null;
			      //��ȡ��Ҫ�޸ĵ�id
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
			                    //�ж��ֶ��Ƿ���ڣ��������޸ģ����������޸�
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
	//ɾ��
	@Override
	public void delect(Entity e)  {
	     //delete from ���� where id = ?;
		//(��ȡ��)�ж����ĸ���ʹ��delect����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
		try {
			Object objectInstance = clazz.newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//�����ֶ�
		String fields = "";
		//������ֵ
		String value="";
		//�м������
		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
	      Object o = "";
	      conn = ConnectionManager.getConnection();
	      PreparedStatement ps  =  null;
	      //��ȡ��Ҫɾ����id
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
	//��ѯ   select*from ����
	@Override
	public List<Entity> selectAll(Entity e)  {
		 //(��ȡ��)�ж����ĸ���ʹ��selectAll����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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
	//��ѯ   select*from ���� where id
	@Override
	public Entity selectById(Entity e)  {
		 //(��ȡ��)�ж����ĸ���ʹ��selectById����
			clazz = e.getClass();
		   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
			tableName = clazz.getSimpleName().toLowerCase();
			//��ȡʵ���෽��
			methods  = clazz.getDeclaredMethods();
			//��ȡ��Ӧ������
			field = clazz.getDeclaredFields();
			//��ȡʵ����Ķ���
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
					 //��ȡ�û���id��װ��SQL�����
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
			            //��ȡ���ݿ�Ԫ����
			            ResultSetMetaData rsmd = rs.getMetaData();
			            List<String> columnList = new ArrayList<String>();
			            //ͨ���е������������Ӧ�������������ڼ�����
			            for(int i = 0;i<rsmd.getColumnCount();i++){
			        columnList.add(rsmd.getColumnName(i+1));
			            }
			            while(rs.next()){
			                for(int i = 0;i<columnList.size();i++){
			                    //��ȡ�����������
			                    String column = columnList.get(i);
			                    //���������и�����set����
			                    String setMethod = "set"+column.substring(0, 1).toUpperCase()+column.substring(1);
			                    //��ȡ�����еķ�����
			                    for(int j=0;j <methods.length;j++){
			                        //ȡ��method����
			                        Method m = methods[j];
			                        //�жϷ����ȡ���еķ������Ƿ�������������и�ɵ�set������ͬ
			                if(m.getName().equals(setMethod)){
			                            //����set������װ����
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
										}//����������ȡ������ж�Ӧ��ֵ
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
		 //(��ȡ��)�ж����ĸ���ʹ��selectLike����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
            //�����ֶ�
      		String fields = "";
      		//������ֵ
      		String value="";
      		//�м������
      		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
      	      Object o = "";
      	    for(int i = 0;i<field.length;i++)
  	      {
  	    	  if(!field[i].getName().equalsIgnoreCase("id"))
  	    	  {
  	    		  //insert  into  ���� ���ֶΣ� VALUES ����ֵ��
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
  	      //ȥ����
  	      fields = fields.substring(1);
  	      value = (String) o;
  	      value = value.substring(1);
  	    
//        SELECT * FROM tb_stu WHERE sname = 'С��'
//        SELECT * FROM tb_stu WHERE sname like '��%'
//        SELECT * FROM tb_stu WHERE sname like '%����Ա'
//        SELECT * FROM tb_stu WHERE sname like '%PHP%'
//	      sql = SQL_INSERT + "into" + tableName+"("+fields+")"+"values("+value+")";

        sql="select * from "+tableName+" where"+"("+fields+")"+" like ("+value+")";
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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

		 //(��ȡ��)�ж����ĸ���ʹ��selectOrderBy����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
            //�����ֶ�
      		String fields = "";
      		//������ֵ
      		String value="";
      		//�м������
      		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
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
  	      //ȥ����
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
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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
		

		 //(��ȡ��)�ж����ĸ���ʹ��selectLimit����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
           //�����ֶ�
     		String fields = "";
     		//������ֵ
     		String value="";
     		//�м������
     		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
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
 	      //ȥ����
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
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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

		 //(��ȡ��)�ж����ĸ���ʹ��selectBetweenAnd����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
            //�����ֶ�
      		String fields = "";
      		//������ֵ
      		String value="";
      		//�м������
      		//(�ڱ�����ʱ��ѭ����ֵinvoke����ֵǿתΪString��ʱ������ݶ�ʧ)
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
  	      //ȥ����
  	      fields = fields.substring(1);
  	      value = (String) o;
  	      value = value.substring(1);
  	    
//  		SELECT "��λ��" 
//  		FROM " �����" 
//  		WHERE "��λ��" BETWEEN 'ֵһ' AND 'ֵ��';


        sql=SQL_SELECT+" ("+fields+") "+" from "+tableName+" where "+" ("+fields+") "+ " between " +"("+value+")"+" and "+"("+value+")";
        conn = ConnectionManager.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
			ps = conn.prepareStatement(sql);
			 rs = ps.executeQuery();
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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

		 //(��ȡ��)�ж����ĸ���ʹ��selectCount����
		clazz = e.getClass();
	   //(��ȡ����)�Ȼ�ȡ������Ȼ������ת��ΪСд�ı���
		tableName = clazz.getSimpleName().toLowerCase();
		//��ȡʵ���෽��
		methods  = clazz.getDeclaredMethods();
		//��ȡ��Ӧ������
		field = clazz.getDeclaredFields();
		//��ȡʵ����Ķ���
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
		        //��ȡ���ݿ��Ԫ����
		        ResultSetMetaData rsmd = rs.getMetaData();
		        //�����������ݿ��ж�Ӧ�������
	            List<String>columnList = new ArrayList<String>();
	            for (int i = 0; i < rsmd.getColumnCount(); i++) {
	               columnList.add(rsmd.getColumnName(i + 1));
	            } 
	            //ѭ��������¼
	            while(rs.next()){
	                //������װ��¼�Ķ���
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
	                // ����һ����¼�е�������
	                for(int i = 0;i<columnList.size();i++){
	                     // ��ȡ����
	                    String column = columnList.get(i);
	                    // ������������set����
	                    String setMethd = "set" + column.substring(0, 1).toUpperCase() + column.substring(1);
	                    // ѭ������methods
	                    for (int j = 0; j < methods.length; j++) {
	                    // ��ȡÿһ��method����
	                    Method m = methods[j];
	                    // �ж�m�ж�Ӧ�ķ����������ݿ�������������set�������Ƿ���ͬ
	                if(m.getName().equals(setMethd)){
	                            // ����set������װ����
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
								}// ��ȡrs�ж�Ӧ��ֵ����װ��obj��
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

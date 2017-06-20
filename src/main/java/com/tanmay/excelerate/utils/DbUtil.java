package com.tanmay.excelerate.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * 
 * @author tanmay
 *
 */
public class DbUtil {

		public static void closeSession(Session session)
	{
		if(session!=null)
		{
			session.flush();
			session.close();
		}
	}
	public static void closePreparedStatement(PreparedStatement pstmt)
	{
		if(pstmt!=null)
		{
			try {
				pstmt.close();
				pstmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void rollBackTransaction(Transaction tx)
	{
		if(tx!=null)
		{
			tx.rollback();
		}
	}
}
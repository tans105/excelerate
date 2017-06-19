package com.tanmay.excelerate.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.tanmay.excelerate.utils.DbUtil;
import com.tanmay.excelerate.utils.HibernateUtils;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
public class AppDao {
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> executeSQlQueryReturnAsListOfMaps(String sqlQuery) {

		Session session = null;
		Transaction tx = null;
		List<Map<String, Object>> list = null;
		try {
			SessionFactory sf = HibernateUtils.getSessionFactory(); // Create session factory which is defined in hibernate.cfg.xml
			session = sf.openSession();
			tx = session.beginTransaction();

			Query query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			list = (List<Map<String, Object>>) query.list();
			tx.commit();
			tx = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeSession(session);
			DbUtil.rollBackTransaction(tx);
		}
		return list;
	}

	public boolean saveOrUpdateEntity(Object obj) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtils.getSessionFactory().openSession();

			tx = session.beginTransaction();
			session.saveOrUpdate(obj);
			tx.commit();
			tx = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeSession(session);
			DbUtil.rollBackTransaction(tx);
		}
		return false;
	}

}

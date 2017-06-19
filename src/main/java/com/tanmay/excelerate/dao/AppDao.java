package com.tanmay.excelerate.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.tanmay.excelerate.entity.ReportFailureArchive;
import com.tanmay.excelerate.utils.DbUtil;
import com.tanmay.excelerate.utils.HibernateUtils;

/**
 * @author : tanmay
 * @created : 19-Jun-2017
 */
public class AppDao {

	public <T> T getEntityByProperty(Map<String, Object> property, Class<T> clazz) {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtils.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(clazz);
			for (Map.Entry<String, Object> entry : property.entrySet()) {
				criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));
			}
			criteria.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<Object> entityList = criteria.list();
			tx.commit();
			tx = null;

			if (entityList != null && entityList.size() > 0) {
				Object obj = entityList.get(0);
				return clazz.cast(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.closeSession(session);
			DbUtil.rollBackTransaction(tx);
		}
		return null;
	}

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

	@SuppressWarnings({ "rawtypes" })
	public List fetchAllReport() {
		Session session = null;
		Transaction tx = null;
		List reports;
		try {
			session = HibernateUtils.getSessionFactory().openSession();
			tx = session.beginTransaction();
			reports = session.createQuery("from ReportManager as reports where reports.active='true'").list();
			tx.commit();
			tx = null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DbUtil.closeSession(session);
			DbUtil.rollBackTransaction(tx);
		}
		return reports;
	}

	public void logFailure(Long reportId, String errorMsg) {
		ReportFailureArchive archive = new ReportFailureArchive();
		archive.setReportId(reportId);
		archive.setFailureRemarks(errorMsg);
		archive.setFailureDtm(new Date());
		Session session = null;
		Transaction tx = null;
		try {
			session = HibernateUtils.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.save(archive);
			tx.commit();
			tx = null;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			{
				if (tx != null) {
					tx.rollback();
					tx = null;
				}
				DbUtil.closeSession(session);
			}
		}
	}

}

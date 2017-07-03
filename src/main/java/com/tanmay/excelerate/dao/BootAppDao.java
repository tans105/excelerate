package com.tanmay.excelerate.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */

@Transactional
@Repository
public class BootAppDao implements IBootAppDao {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportManager> fetchAllReport() {
		String hql = "FROM ReportManager as report where report.active='true'";
		return (List<ReportManager>) entityManager.createQuery(hql).getResultList();
	}

	@Override
	public void logFailure(ReportManager report, String string) {
		
	}

	
	
	
}

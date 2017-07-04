package com.tanmay.excelerate.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tanmay.excelerate.entity.ReportFailureArchive;
import com.tanmay.excelerate.entity.ReportManager;

/**
 * @author : tanmay
 * @created : 03-Jul-2017
 */

@Transactional
@Repository
public class AppDaoImpl implements AppDao {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportManager> fetchAllReport() {
		String hql = "FROM ReportManager as report where report.active='true'";
		return (List<ReportManager>) entityManager.createQuery(hql).getResultList();
	}

	@Override
	public void logFailure(ReportManager report, String errorMsg) {
		report.setIsFailing(Boolean.TRUE);
		report.setLastFailureDtm(new Date());
		entityManager.merge(report);
		ReportFailureArchive archive = new ReportFailureArchive();
		archive.setReportId(report.getReportId());
		archive.setFailureRemarks(errorMsg);
		archive.setFailureDtm(new Date());
		entityManager.persist(archive);

	}

	@Override
	public void saveOrUpdateEntity(ReportManager report) {
		entityManager.merge(report);

	}

}

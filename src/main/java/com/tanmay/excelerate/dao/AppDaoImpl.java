package com.tanmay.excelerate.dao;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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

	@Autowired
	JdbcTemplate jdb;
	
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object[] getColumnNames(String query) {
		List columns = new ArrayList();
	    jdb.query(query,new ResultSetExtractor() {
	 
	        @Override
	        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnCount = rsmd.getColumnCount();
	            for(int i = 1 ; i <= columnCount ; i++){
	                columns.add(rsmd.getColumnName(i));
	            }
	            return columnCount;
	        }
	    });
		return columns.toArray();
	}

	@Override
	public List<Map<String, Object>> extractQueryResult(String query) {
		return jdb.queryForList(query);
	}

}

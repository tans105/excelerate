package com.tanmay.excelerate.utils;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * @author : tanmay
 * @created : 14-Jun-2017
 */

@SuppressWarnings("deprecation")
public class HibernateUtils {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		Properties dbConnectionProperties = new Properties();
		
		try {
			dbConnectionProperties.load(HibernateUtils.class.getClassLoader().getSystemClassLoader().getResourceAsStream("dbconnection.properties"));
			return new AnnotationConfiguration().mergeProperties(dbConnectionProperties).configure().buildSessionFactory();          

		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}

package com.collabera.jbdcdemo.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.collabera.jdbcdemo.dao.EmployeeDao;

public class EmployeeDaoDeleteTest {
	
	@Test
	public void testDelete() {
		boolean deleted = false;
		EmployeeDao employeeDao = new EmployeeDao();
		try {
			deleted = employeeDao.delete(46);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(deleted);
	}

}

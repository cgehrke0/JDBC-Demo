package com.collabera.jbdcdemo.dao;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.collabera.jbdcdemo.model.Department;
import com.collabera.jbdcdemo.model.Employee;
import com.collabera.jdbcdemo.dao.EmployeeDao;

public class EmployeeDaoInsertTest {
	
	@Test
	public void testInset() {
		EmployeeDao employeeDao = new EmployeeDao();
		boolean inserted = false;
		try {
			inserted = employeeDao.insert(new Employee(46, "Jon6", "Snow", Department.DEVELOPMENT));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		assertTrue(inserted);
	}

}

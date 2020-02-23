package com.collabera.jbdcdemo.dao;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.collabera.jbdcdemo.model.Employee;
import com.collabera.jdbcdemo.dao.EmployeeDao;

public class EmployeeDaoGetAllTest {
	@Test
	public void testGetAllEmployees() throws FileNotFoundException, IOException {
		EmployeeDao employeeDao = new EmployeeDao();
		List<Employee> list = new ArrayList<Employee>();
		try {
			list = employeeDao.getAllEmployee();
		} catch(SQLException e1) {
			e1.printStackTrace();
		}
		for(Employee e: list)
			System.out.println(e);
		
		System.out.println("------------------------------");
		assertTrue(list.size()>0);
	}

}

package com.collabera.jdbcdemo.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.day14maven.App;
import com.collabera.jbdcdemo.model.City;
import com.collabera.jbdcdemo.model.Country;
import com.collabera.jbdcdemo.model.Employee;
import com.collabera.jdbcdemo.utils.JdbcUtils;
import com.collabera.jbdcdemo.model.Department;



public class EmployeeDao {
	private static final Logger logger = Logger.getLogger(EmployeeDao.class.getName());
	private static HashMap<Integer,Employee> cache = new HashMap();
	
	public List<Employee> getAllEmployee() throws FileNotFoundException, IOException, SQLException{
		Properties props = new Properties();
		props.load(new FileInputStream("jdbc.properties"));
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
	
		List<Employee> list = new ArrayList<Employee>();
		
		PreparedStatement pstmt = DriverManager
				.getConnection(dburl, user, password).prepareStatement("SELECT * FROM employees" );
		try {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Employee e = resultSetToEmployee(rs);
				list.add(e);
			}
		}catch (SQLException sqle) {
			logger.error("error executing: "+ sqle);
		} finally {
			if (pstmt!=null) try { pstmt.close();}
			catch(SQLException e) { /* ignore it * */}
		}
		return list;
	}
	private Employee resultSetToEmployee(ResultSet rs) throws SQLException{
		Employee employee = null;
		
		Integer id = rs.getInt("employee_id");
		if(cache.containsKey(id)) {
			employee = cache.get(id);
		} else {
			employee = new Employee();
		}
		
		employee.setId(id);
		employee.setLastName(rs.getString("lastName"));
		employee.setFirstName(rs.getString("firstName"));
		Department[] departs = Department.values();
		int index = rs.getInt("department_id")-1;
		employee.setDepartment(departs[index]);
		
		if(!cache.containsKey(id)) {
			cache.put(id,  employee);
		}
		logger.info( "test");
		
		return employee;
	}
	public boolean insert(Employee employee) throws SQLException{
		if(employee==null) return false;
		logger.info("test");
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("INSERT INTO employees(employee_id, firstName, lastName, department_id) VALUES(?,?,?,?)");
		
		statement.setInt(1,  employee.getId());
		statement.setString(2,  employee.getFirstName());
		statement.setString(3, employee.getLastName());
		statement.setInt(4,  employee.getDepartment().ordinal()+1);
		int count = 0;
		
		try {
			count = statement.executeUpdate();
		} catch(SQLException sqle) {
			logger.error("error executing insert for employee: " + employee);
		} finally {
			statement.close();
		}
		return count > 0;
	}
	
	public boolean delete(int id) throws SQLException {
		//if (id==null) return false;
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("DELETE FROM Employees where employee_id = ?" );
		statement.setInt(1,  id);;
		
		int count = 0;
		if (statement==null) return false;
		
		try {
			count = statement.executeUpdate();
		} catch (SQLException sqle) {
			logger.error("error executing delete for employee_id: " + id);
		} finally {
			statement.close();
		}
		return count > 0;
	}
	static Logger log = Logger.getLogger(EmployeeDao.class.getName());
	public static void main(String[] args) {
		
		EmployeeDao employeeDao = new EmployeeDao();
		/*try {
			List<Country> countries = countryDao.findByName("Congo");
			if(countries.size()>0) {
				log.info(countries.get(0));
			}
		} catch(Exception e) {
			log.error(e.getMessage());
		}
		*/
			EmployeeDao employeeDao2 = new EmployeeDao();
			try {
				List<Employee> employees = employeeDao2.getAllEmployee();
			}catch (Exception e) {
				log.error(e.getMessage());
			}
		try {
			//Employee employee = new Employee(13,"Dawson","Gehrke", Department.values()[3]);
			boolean success = employeeDao.delete(12);
			//Country country = new Country("AAA", "DDD", "Asia", "WWW");
			//boolean success = countryDao.insert(country);
			if(success)
				//log.info("SUCCESS: INSERT employee: " + employee);
				log.info("SUCCESS: Delete employee: " + 12);
			//String code = "XYZ";
			//		boolean count = countryDao.delete(code);
			//log.info("SUCCESS: Delete country: " + code);
		}catch (Exception x) {
			log.error(x.getMessage());
	}

	}

}

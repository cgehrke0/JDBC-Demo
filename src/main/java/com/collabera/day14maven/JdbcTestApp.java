package com.collabera.day14maven;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.jbdcdemo.model.City;
import com.collabera.jbdcdemo.model.Country;
import com.collabera.jbdcdemo.model.Department;
import com.collabera.jbdcdemo.model.Employee;
import com.collabera.jdbcdemo.dao.CityDao;
import com.collabera.jdbcdemo.dao.CountryDao;
import com.collabera.jdbcdemo.dao.EmployeeDao;

import jdk.internal.org.jline.utils.Log;

public class JdbcTestApp {
	static Logger log = Logger.getLogger(App.class.getName());
	public static void main(String[] args) throws FileNotFoundException, IOException {
		log.debug("Hello World!");
		log.info("JDBC test app started..");
		// connect to database
	  	Properties props = new Properties();
    	props.load(new FileInputStream("jdbc.properties"));
    	
    	String user = props.getProperty("user");
    	String password = props.getProperty("password");
    	String dburl = props.getProperty("dburl");
		Connection conn = null;
		try {
			conn = (Connection) DriverManager.getConnection(dburl, user, password);
					if(conn!=null) {
						System.out.println("mysql connection successfully aquired!");
					}
			/*String sql = "select * from city";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				log.debug(rs.getInt(1) + " " + rs.getString(2) +
						" " + rs.getString(3) + " " + rs.getString(4));
			}
			
			rs.close();*/
			conn.close();
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		
		//test CityDao
		//log.info("Testing CityDao . . . ");
		
		/*CityDao cityDao = new CityDao();
		try {
			List<City> cities = cityDao.findByName("Miami");
			if(cities.size() > 0) {
				log.info(cities.get(0));
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		
		
		
		CountryDao countryDao = new CountryDao();*/
		log.info("Testing EmployeeDao ... ");
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
		try {
			Employee employee = new Employee(12,"Carter","Gehrke", Department.values()[3]);
			boolean success = employeeDao.insert(employee);
			//Country country = new Country("AAA", "DDD", "Asia", "WWW");
			//boolean success = countryDao.insert(country);
			if(success)
				log.info("SUCCESS: INSERT employee: " + employee);
			//String code = "XYZ";
			//		boolean count = countryDao.delete(code);
			//log.info("SUCCESS: Delete country: " + code);
		}catch (Exception x) {
			log.error(x.getMessage());
	}

}}

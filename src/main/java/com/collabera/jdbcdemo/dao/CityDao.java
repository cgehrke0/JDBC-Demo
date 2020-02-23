package com.collabera.jdbcdemo.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.collabera.jbdcdemo.model.City;
import java.util.*;

public class CityDao {
	Properties props = new Properties();
	
	String user = props.getProperty("user");
	String password = props.getProperty("password");
	String dburl = props.getProperty("dburl");
	private static final Logger logger = Logger.getLogger(CityDao.class.getName());
	private static HashMap<Integer,City> cache = new HashMap();
	
	public City findById(Integer id) throws SQLException {
		if(cache.containsKey(id)) return cache.get(id);
		List<City> list = find("WHERE  id = "+id);
		return list.get(0);
	}
	
	public List<City> findByName(String name) throws SQLException{
		name = sanitize(name);
		List<City> list = find("WHERE name - ' "+name+"'");
		return list;
	}
	
	public List<City> find(String query) throws SQLException {
		List<City> list = new ArrayList<City>();
		Statement stmt = DriverManager
				.getConnection(dburl, user, password).createStatement();
		String sqlquery = "SELECT * FROM city c " + query;
		try {
			logger.debug("executing query: " + sqlquery);
			ResultSet rs = stmt.executeQuery(sqlquery);
			while (rs.next()) {
				City c = resultSetToCity(rs);
				list.add(c);
			}
		}catch (SQLException sqle) {
			logger.error("error executing: "+sqlquery, sqle);
		} finally {
			if (stmt!=null) try { stmt.close();}
			catch(SQLException e) { /* ignore it * */}
			return list;
		}
	}
	private City resultSetToCity(ResultSet rs) throws SQLException{
		City city = null;
		
		Integer id = rs.getInt("id");
		if(cache.containsKey(id)) {
			city = cache.get(id);
		} else {
			city = new City();
		}
		
		city.setId(id);
		city.setName(rs.getString("Name"));
		city.setDistrict(rs.getString("District"));
		city.setPopulation(rs.getInt("Population"));
		String countrycode = rs.getString("countrycode");
		
		if(!cache.containsKey(id)) {
			cache.put(id,  city);
		}
		logger.info("get country for city " + city.getName());
		
		return city;
	}
	
	private String sanitize(String name) {
		return name;
	}

}

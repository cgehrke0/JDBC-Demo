package com.collabera.jdbcdemo.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.jbdcdemo.model.City;
import com.collabera.jbdcdemo.model.Country;
import com.collabera.jdbcdemo.utils.JdbcUtils;

public class CountryDao {
	private static final Logger logger = Logger.getLogger(CountryDao.class.getName());
	private static HashMap<String,Country> cache = new HashMap();
	
	public List<Country> findByName(String name) throws SQLException, FileNotFoundException, IOException{
		Properties props = new Properties();
		props.load(new FileInputStream("jdbc.properties"));
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
	
		List<Country> list = new ArrayList<Country>();
		
		PreparedStatement pstmt = DriverManager
				.getConnection(dburl, user, password).prepareStatement("SELECT * FROM Country where name = ?" );
		pstmt.setString(1, "Thailand");
		try {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Country c = resultSetToCountry(rs);
				list.add(c);
			}
		}catch (SQLException sqle) {
			logger.error("error executing: "+ sqle);
		} finally {
			if (pstmt!=null) try { pstmt.close();}
			catch(SQLException e) { /* ignore it * */}
		}
		return list;
	}
	private Country resultSetToCountry(ResultSet rs) throws SQLException{
		Country country = null;
		
		String code = rs.getString("code");
		if(cache.containsKey(code)) {
			country = cache.get(code);
		} else {
			country = new Country(code, code, code, code);
		}
		
		country.setCode(code);
		country.setName(rs.getString("Name"));
		country.setContinent(rs.getString("Continent"));
		country.setRegion(rs.getString("Region"));
		
		if(!cache.containsKey(code)) {
			cache.put(code,  country);
		}
		logger.info("get country for city " + country.getName());
		
		return country;
	}
	public boolean delete(String code) throws SQLException {
		if (code==null) return false;
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("DELETE FROM Country where code = ?" );
		statement.setString(1,  code);;
		
		int count = 0;
		if (statement==null) return false;
		
		try {
			count = statement.executeUpdate();
		} catch (SQLException sqle) {
			logger.error("error executing delete for code: " + code);
		} finally {
			statement.close();
		}
		return count > 0;
	}
	
	public boolean insert(Country country) throws SQLException{
		if(country==null) return false;
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("INSERT INTO country(code, name, continent, region) VALUES(?,?,?,?)");
		
		statement.setString(1,  country.getCode());
		statement.setString(2,  country.getName());
		statement.setString(3, country.getContinent());
		statement.setString(4,  country.getRegion());
		int count = 0;
		try {
			count = statement.executeUpdate();
		} catch(SQLException sqle) {
			logger.error("error executing insert for country: " + country);
		} finally {
			statement.close();
		}
		return count > 0;
	}
	}

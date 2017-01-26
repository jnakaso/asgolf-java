package com.jnaka.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.jnaka.domain.EntityObject;

abstract public class JdbcDao<T extends EntityObject> implements ObjectDao<T> {

	private static final int MAX_TRIES = 3;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private JdbcTemplate jdbcTemplate;
	private RowMapper<T> mapper;

	abstract protected String getDefaultOrderClause();

	abstract protected String getTableName();

	abstract protected String getIdColumnName();

	abstract protected String getInsertQueryString(T anObject);

	abstract protected String getUpdateQueryString(T anObject);

	protected Logger getLogger() {
		return logger;
	}

	protected RowMapper<T> getRowMapper() throws SQLException {
		if (this.mapper == null)
			this.setMapper(this.defaultMapper());
		return this.mapper;
	}

	protected void setMapper(RowMapper<T> defaultMapper) {
		this.mapper = defaultMapper;
	}

	abstract protected RowMapper<T> defaultMapper();

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected String getIdentityClause(T anObject) {
		StringBuffer buffer = new StringBuffer(" ");
		buffer.append(this.getIdColumnName());
		buffer.append(" = ");
		buffer.append(anObject.getId());
		return buffer.toString();
	}

	protected String getSelectQueryString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT *");
		buffer.append(" FROM");
		buffer.append(" " + this.getTableName());
		buffer.append(" " + this.getDefaultOrderClause());
		return buffer.toString();
	}

	protected String getCountQueryString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT count(*)");
		buffer.append(" FROM " + this.getTableName());
		return buffer.toString();
	}

	private String getLastIdQueryString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT max(" + this.getIdColumnName() + ")");
		buffer.append(" FROM " + this.getTableName());
		return buffer.toString();
	}

	protected String getSelectQueryString(Number id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT *");
		buffer.append(" FROM " + this.getTableName());
		buffer.append(" WHERE " + this.getIdColumnName());
		buffer.append(" = " + id);
		return buffer.toString();
	}

	public void update(T anObject) {
		String qString = this.getUpdateQueryString(anObject);
		this.getJdbcTemplate().execute(qString);

	}

	public void create(T anObject) {
		Number lastId = this.getLastId();
		String qString = this.getInsertQueryString(anObject);
		this.getJdbcTemplate().execute(qString);
		int tries = 0;
		// TODO transaction isolation
		while (tries < MAX_TRIES) {
			Integer newId = this.getLastId();
			if (lastId.equals(newId))
				tries++;
			else {
				anObject.setId(newId);
				this.getLogger().info("Creating ID: " + this.getTableName() + ", id= " + newId);
				return;
			}
		}
		throw new RuntimeException("Database didn't put it in, gave up after " + MAX_TRIES + " attempts");
	}

	public int getCount() {
		String queryString = this.getCountQueryString();
		return this.getJdbcTemplate().queryForInt(queryString);
	}

	public int getLastId() {
		String queryString = this.getLastIdQueryString();
		return this.getJdbcTemplate().queryForInt(queryString);
	}

	public List<T> getAll() {
		return this.find(this.getSelectQueryString());
	}

	public List<T> find(String queryString) {
		try {
			return this.getJdbcTemplate().query(queryString, this.getRowMapper());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public T load(Number id) {
		try {
			String queryString = this.getSelectQueryString(id);
			return (T) this.getJdbcTemplate().queryForObject(queryString, this.getRowMapper());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

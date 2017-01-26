package com.jnaka.golf.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnaka.dao.ObjectDao;
import com.jnaka.domain.EntityObject;
import com.jnaka.golf.domain.GolfClub;

abstract public class ObjectJaxbDao<T extends EntityObject> implements ObjectDao<T> {

	@Autowired
	@Qualifier("DataStore")
	private JaxbDataStore dataStore;

	public JaxbDataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(JaxbDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public GolfClub getGolfClub() {
		return this.getDataStore().getGolfClub();
	}

	@SuppressWarnings("unchecked")
	public T load(final Number id) {
		return (T) CollectionUtils.find(this.getAll(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				T object = (T) arg0;
				return object.getId().equals(id);
			}
		});
	}

	@Override
	public int getCount() {
		return this.getAll().size();
	}

	@Override
	public int getLastId() {
		List<T> all = this.getAll();
		return all.get(all.size() - 1).getId().intValue();
	}

	public void update(T anObject) {
		// TODO marshall the file ?
	}
}

/*
 * Copyright (C) 2011 Therap (BD) Ltd.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bd.gov.forms.dao;

import bd.gov.forms.domain.ListData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author asif
 */
@Repository("listDao")
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ListDaoImpl implements ListDao {
    
    private static final Logger log = LoggerFactory.getLogger(ListDaoImpl.class);

    @Autowired
    private DefaultLobHandler lobHandler;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionFactory sessionFactory;
    
    public String getListDataSysId(int id) {
        /*return (String) jdbcTemplate.queryForObject(
                "SELECT sys_id FROM list_data WHERE id = ?",
                new Object[]{id},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("sys_id");
                    }
                });*/
        return (String) sessionFactory.getCurrentSession().createCriteria(ListData.class)
                .setProjection(Property.forName("sysId"))
                .add(Property.forName("id").eq(id)).list().get(0);
    }

    public ListData getListData(String sysId) {
        /*return (ListData) jdbcTemplate.queryForObject(
                "SELECT * FROM list_data WHERE sys_id = ?",
                new Object[]{sysId},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        ListData list = new ListData();

                        list.setId(rs.getInt("id"));
                        list.setSysId(rs.getString("sys_id"));
                        list.setName(rs.getString("name"));
                        list.setDetail(rs.getString("detail"));
                        list.setValues(rs.getString("list_values"));

                        return list;
                    }
                });*/
        return (ListData) sessionFactory.getCurrentSession().createCriteria(ListData.class)
                .add(Restrictions.eq("sysId", sysId)).list().get(0);
    }

    public List getListDataList() {
        /*return jdbcTemplate.query(
                "SELECT * FROM list_data ",
                new Object[]{},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        ListData list = new ListData();

                        list.setId(rs.getInt("id"));
                        list.setSysId(rs.getString("sys_id"));
                        list.setName(rs.getString("name"));
                        list.setDetail(rs.getString("detail"));

                        return list;
                    }
                });*/
        return sessionFactory.getCurrentSession().createCriteria(ListData.class).list(); 
    }

    public void saveListData(final ListData listData) {
        /*String sql = "INSERT INTO list_data";
        sql += " (sys_id, name, detail, list_values";
        sql += ")";
        sql += " VALUES (?, ?, ?, ?";
        sql += ")";

        log.debug("SQL INSERT query: {}", sql);

        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        int i = 1;

                        ps.setString(i++, listData.getSysId());
                        ps.setString(i++, listData.getName());
                        ps.setString(i++, listData.getDetail());
                        ps.setString(i, listData.getValues());
                    }
                }
        );*/
        sessionFactory.getCurrentSession().save(listData);
    }
    
    @Transactional(readOnly=false)
    public void updateListData(final ListData lst) {
        /*String sql = "UPDATE list_data";
        sql += " set name = ?, detail = ?, list_values = ?";
        sql += " WHERE sys_id = ?";

        log.debug("SQL UPDATE query: {}", sql);

        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        int i = 1;

                        ps.setString(i++, lst.getName());
                        ps.setString(i++, lst.getDetail());
                        ps.setString(i++, lst.getValues());
                        ps.setString(i, lst.getSysId());
                    }
                }
        );*/
        ListData lstData = (ListData) sessionFactory.getCurrentSession().createCriteria(ListData.class)
                .add(Property.forName("sysId").eq(lst.getSysId())).list().get(0);
        
        lstData.setName(lst.getName());
        lstData.setDetail(lst.getDetail());
        lstData.setValues(lst.getValues());
        
        sessionFactory.getCurrentSession().update(lstData);
    }
}

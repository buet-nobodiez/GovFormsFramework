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

import bd.gov.forms.domain.*;
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
@Repository("userDao")
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    
    @Autowired
    DefaultLobHandler lobHandler;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SessionFactory sessionFactory;
    
    public User getUser(String sysId) {
        /*return (User) jdbcTemplate.queryForObject(
                "SELECT * FROM user WHERE sys_id = ?",
                new Object[]{sysId},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();

                        user.setId(rs.getInt("id"));
                        user.setSysId(rs.getString("sys_id"));
                        user.setName(rs.getString("name"));
                        user.setTitle(rs.getString("title"));
                        user.setUserName(rs.getString("user"));
                        user.setPassword(rs.getString("password"));
                        user.setMinistryName(rs.getString("ministry"));

                        user.setMobile(rs.getString("mobile"));
                        user.setEmail(rs.getString("email"));
                        user.setAdmin(rs.getInt("admin"));
                        user.setActive(rs.getInt("active"));

                        return user;
                    }
                });*/
        return (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Property.forName("sysId").eq(sysId)).list().get(0);
    }

    public User getUser(String userName, String password) {
        /*try {
            return (User) jdbcTemplate.queryForObject(
                    "SELECT * FROM user WHERE user = ? and password=?",
                    new Object[]{userName, password},
                    new RowMapper() {

                        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            User user = new User();

                            user.setId(rs.getInt("id"));
                            user.setSysId(rs.getString("sys_id"));
                            user.setName(rs.getString("name"));
                            user.setTitle(rs.getString("title"));
                            user.setUserName(rs.getString("user"));
                            user.setMinistryName(rs.getString("ministry"));

                            user.setMobile(rs.getString("mobile"));
                            user.setEmail(rs.getString("email"));
                            user.setAdmin(rs.getInt("admin"));
                            user.setActive(rs.getInt("active"));

                            return user;
                        }
                    });
        } catch (Exception ex) {
            log.debug("Exception in getUser().", ex);
            return null;
        }*/
        try {
            
            return (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Property.forName("userName").eq(userName))
                .add(Property.forName("password").eq(password)).list().get(0);
            
        } catch (Exception ex) {
            log.debug("Exception in getUser().", ex);
            return null;
        }
    }

    public User getUserWithEmail(String userName, String email) {
        /*try {
            return (User) jdbcTemplate.queryForObject(
                    "SELECT * FROM user WHERE user = ? and email=?",
                    new Object[]{userName, email},
                    new RowMapper() {

                        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                            User user = new User();

                            user.setId(rs.getInt("id"));
                            user.setSysId(rs.getString("sys_id"));
                            user.setName(rs.getString("name"));
                            user.setTitle(rs.getString("title"));
                            user.setUserName(rs.getString("user"));
                            user.setMinistryName(rs.getString("ministry"));
                            
                            user.setMobile(rs.getString("mobile"));
                            user.setEmail(rs.getString("email"));
                            user.setAdmin(rs.getInt("admin"));
                            user.setActive(rs.getInt("active"));

                            return user;
                        }
                    });
        } catch (Exception ex) {
            log.debug("Exception in getUserWIthEmail().", ex);
            return null;
        }*/
        try {
           
            return (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                    .add(Property.forName("userName").eq(userName))
                    .add(Property.forName("email").eq(email)).list().get(0);
            
        } catch (Exception ex) {
            log.debug("Exception in getUserWIthEmail().", ex);
            return null;
        }
    }

    public List getUserList() {
        /*return jdbcTemplate.query(
                "SELECT * FROM user ",
                new Object[]{},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();

                        user.setSysId(rs.getString("sys_id"));
                        user.setName(rs.getString("name"));
                        user.setTitle(rs.getString("title"));
                        user.setUserName(rs.getString("user"));
                        user.setAdmin(rs.getInt("admin"));
                        user.setActive(rs.getInt("active"));

                        return user;
                    }
                });*/
        return sessionFactory.getCurrentSession().createCriteria(User.class).list();
    }
    
    public List getUserList(final String ministryName) {
        /*return jdbcTemplate.query(
                "SELECT * FROM user where ministry = ? ",
                new Object[]{ministryName},
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();

                        user.setSysId(rs.getString("sys_id"));
                        user.setName(rs.getString("name"));
                        user.setTitle(rs.getString("title"));
                        user.setUserName(rs.getString("user"));
                        user.setAdmin(rs.getInt("admin"));
                        user.setActive(rs.getInt("active"));

                        return user;
                    }
                });*/
        return sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Property.forName("ministryName").eq(ministryName)).list();
    }

    public void saveUser(final User user) {
        /*String sql = "INSERT INTO user";
        sql += " (sys_id, name, title, user, password, mobile, email, admin, active, ministry";
        sql += ")";
        sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        sql += ")";

        log.debug("SQL INSERT query: {}", sql);

        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        int i = 1;

                        ps.setString(i++, user.getSysId());
                        ps.setString(i++, user.getName());
                        ps.setString(i++, user.getTitle());
                        ps.setString(i++, user.getUserName());
                        ps.setString(i++, user.getPassword());
                        ps.setString(i++, user.getMobile());
                        ps.setString(i++, user.getEmail());
                        ps.setInt(i++, user.getAdmin());
                        ps.setInt(i++, user.getActive());
                        ps.setString(i, user.getMinistryName());
                    }
                }
        );*/
        sessionFactory.getCurrentSession().save(user);
    }
    
    @Transactional(readOnly=false)
    public void updateUser(final User user) {
        /*String sql = "UPDATE user";
        sql += " set name = ?, title = ?, mobile = ?, email = ?, admin = ?, active = ?, ministry = ?";
        sql += " WHERE sys_id = ?";

        log.debug("SQL UPDATE query: {}", sql);

        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        int i = 1;

                        ps.setString(i++, user.getName());
                        ps.setString(i++, user.getTitle());                   
                        ps.setString(i++, user.getMobile());
                        ps.setString(i++, user.getEmail());
                        ps.setInt(i++, user.getAdmin());
                        ps.setInt(i++, user.getActive());
                        ps.setString(i++, user.getMinistryName() );
                        ps.setString(i, user.getSysId());
                        
                    }
                }
        );*/
        User us = (User) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Property.forName("sysId").eq(user.getSysId())).list().get(0);
        us.setName(user.getName());
        us.setTitle(user.getTitle());
        us.setMobile(user.getMobile());
        us.setEmail(user.getEmail());
        us.setAdmin(user.getAdmin());
        us.setActive(user.getActive());
        us.setMinistryName(user.getMinistryName());
        
        //us = user;
        
        sessionFactory.getCurrentSession().update(us);
    }

    public int changePassword(String userName, String password) {
        return jdbcTemplate.update("UPDATE user SET password = ? WHERE user = ?", password, userName);
    }

    public int getCountWithUserName(String userName) {
        return jdbcTemplate.queryForInt("select count(*) from user where user = ?" , userName);
    }
    
    public void saveMinistry(final String ministry) {
        /*String sql = "insert into ministry(ministry_name) values(?)";
        
        jdbcTemplate.execute(sql,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {

                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
                        int i = 1;

                        ps.setString(i, ministry);
                       
                    }
                }
        );*/
        sessionFactory.getCurrentSession().save(ministry);
    }
    
    public List getMinistryList() {
        /*String sql = "select * from ministry";
        
        return jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Ministry ministry = new Ministry();
                        
                        ministry.setMinistryName( rs.getString("ministry_name"));
                        ministry.setId(rs.getInt("id"));
                        return ministry;
                    }
                });*/
        return sessionFactory.getCurrentSession().createCriteria(Ministry.class).list();
    }
    
    public List getMinistryListString() {
        /*String sql = "select ministry_name from ministry";
        
        return jdbcTemplate.query(sql,
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("ministry_name");
                    }
                });*/
        return sessionFactory.getCurrentSession().createCriteria(Ministry.class)
                .setProjection(Property.forName("ministryName")).list();
    }
    
    
    
    
    
    
    
    
    
    
     
    
    
    
    public void saveMilestone(Milestone milestone) {
        /*int id = sessionFactory.getCurrentSession().createCriteria(Ministry.class)
                .add(Restrictions.eq("ministryName", milestone.getMinistryName()));*/
        
        sessionFactory.getCurrentSession().save(milestone);
    }
    
    public List getMilestoneList() {
        return sessionFactory.getCurrentSession().createCriteria(Milestone.class).list();
    }
    
    public Milestone getMilestone(int id) {
        return (Milestone)sessionFactory.getCurrentSession().createCriteria(Milestone.class)
                .add(Property.forName("id").eq(id)).list().get(0);
    }
    
    public List getMilestoneList(String ministryName, int role){
        return sessionFactory.getCurrentSession().createCriteria(Milestone.class)
                .add(Property.forName("ministryName").eq(ministryName))
                .add(Property.forName("role").eq(role)).list();
    }
    
    public void saveWorkflow(Workflow workflow){
        sessionFactory.getCurrentSession().save(workflow);
    }
    
    public List getWorkflowList(){
        return sessionFactory.getCurrentSession().createCriteria(Workflow.class).list();
    }
    
    public Workflow getWorkflow(int id){
        return null;
    }
    
    
    public void saveWorkflowDetail(WorkFlowDetail workflowDetail){
        sessionFactory.getCurrentSession().save(workflowDetail);
    }
    
    public List getWorkflowDetailList(int workflowId){
        return sessionFactory.getCurrentSession().createCriteria(WorkFlowDetail.class)
                .add(Property.forName("workflowId").eq(workflowId)).list();
    }
    
    public List getTrackList(int milestoneId){
        return sessionFactory.getCurrentSession().createCriteria(Track.class)
                .add(Property.forName("currentMilestoneId").eq(milestoneId)).list();
    }
    
    public Object getWorkflowId(String formId) {
        return  sessionFactory.getCurrentSession().createCriteria(Form.class)
                .setProjection(Property.forName("workflowId"))
                .add(Property.forName("formId").eq(formId)).list().get(0);
    }
    
    public Object getnextMilestoneId(int workflowId, int milestoneId, int milestoneFlag) {
        if (milestoneFlag == 1) {
            return sessionFactory.getCurrentSession().createCriteria(WorkFlowDetail.class)
                    .setProjection(Property.forName("acceptMilestoneId"))
                    .add(Property.forName("workflowId").eq(workflowId))
                    .add(Property.forName("milestoneId").eq(milestoneId)).list().get(0);
        }
        
        if (milestoneFlag == 4) {
            return sessionFactory.getCurrentSession().createCriteria(WorkFlowDetail.class)
                    .setProjection(Property.forName("declineMilestoneId"))
                    .add(Property.forName("workflowId").eq(workflowId))
                    .add(Property.forName("milestoneId").eq(milestoneId)).list().get(0);
        }
        
        return null;
    }
    
    public void saveWorkflowDetailHistory(WorkFlowHistory history) {
        sessionFactory.getCurrentSession().save(history);
    }
    
    public WorkFlowHistory getWorkflowHistory(String trackId){
        return (WorkFlowHistory) sessionFactory.getCurrentSession().createCriteria(WorkFlowHistory.class)
                .add(Property.forName("trackId").eq(trackId))
                .addOrder(Property.forName("id").desc()).list().get(0);
    }
    
    
    public List getUserTrackList(int userId){
        return sessionFactory.getCurrentSession().createCriteria(Track.class)
                .add(Property.forName("userId").eq(userId)).list();
    }
}

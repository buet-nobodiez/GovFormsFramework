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

import bd.gov.forms.domain.Milestone;
import bd.gov.forms.domain.Ministry;
import bd.gov.forms.domain.User;
import bd.gov.forms.domain.WorkFlowDetail;
import bd.gov.forms.domain.WorkFlowHistory;
import bd.gov.forms.domain.Workflow;

import java.util.List;

/**
 * @author asif
 */
public interface UserDao {

    public void saveUser(final User user);

    public User getUser(String sysId);

    public void updateUser(final User user);

    public List getUserList();
    
    public List getUserList(final String ministryName);

    public User getUser(String userName, String password);

    public User getUserWithEmail(String userName, String email);

    public int changePassword(String userName, String password);

    public int getCountWithUserName(String userName);
    
    public void saveMinistry(String ministry);
    
    public List getMinistryList();
    
    public List getMinistryListString();
    
    public void saveMilestone(Milestone milestone);
    
    public List getMilestoneList();
    
    public Milestone getMilestone(int id);
    
    public List getMilestoneList(String ministryName, int role);
    
    public void saveWorkflow(Workflow workflow);
    
    public List getWorkflowList();
    
    public Workflow getWorkflow(int id);
    
    public void saveWorkflowDetail(WorkFlowDetail workflowDetail);
    
    public List getWorkflowDetailList(int workflowId);
    
    public List getTrackList(int milestoneId);
    
    public Object getWorkflowId(String formId);
    
    public Object getnextMilestoneId(int workflowId, int milestoneId, int milestoneFlag);
    
    public void saveWorkflowDetailHistory(WorkFlowHistory history);
    
    public WorkFlowHistory getWorkflowHistory(String trackId);
    
    public List getUserTrackList(int userId);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bd.gov.forms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author humayun
 */

@Entity
@Table(name="track")
public class Track {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="form_id")
    private String formId;
    @Column(name="form_name")
    private String formName;
    @Column(name="track_id")
    private String trackId;
    @Column(name="user_id")
    private int userId;
    @Column(name="current_milestone_id")
    private int currentMilestoneId;
    @Transient
    private WorkFlowHistory workflowHistory;
    @Transient
    private Milestone milestone;

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public WorkFlowHistory getWorkflowHistory() {
        return workflowHistory;
    }

    public void setWorkflowHistory(WorkFlowHistory workflowHistory) {
        this.workflowHistory = workflowHistory;
    }
    
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getCurrentMilestoneId() {
        return currentMilestoneId;
    }

    public void setCurrentMilestoneId(int currentMilestoneId) {
        this.currentMilestoneId = currentMilestoneId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
        
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
    
    
}

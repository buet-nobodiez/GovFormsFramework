/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bd.gov.forms.domain;

import java.util.List;
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
@Table(name="workflow_detail")
public class WorkFlowDetail {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="milestone_id")
    private int milestoneId;
    @Column(name="workflow_id")
    private int workflowId;
    @Column(name="accept_value")
    private int acceptMilestoneId;
    @Column(name="decline_value")
    private int declineMilestoneId;
    
    @Transient
    private String[] abc;
    
    @Transient
    private String milestoneName;
    @Transient
    private String acceptMilestoneName;
    @Transient
    private String declineMilestoneName;

    public String getAcceptMilestoneName() {
        return acceptMilestoneName;
    }

    public void setAcceptMilestoneName(String acceptMilestoneName) {
        this.acceptMilestoneName = acceptMilestoneName;
    }

    public String getDeclineMilestoneName() {
        return declineMilestoneName;
    }

    public void setDeclineMilestoneName(String declineMilestoneName) {
        this.declineMilestoneName = declineMilestoneName;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String[] getAbc() {
        return abc;
    }

    public void setAbc(String[] abc) {
        this.abc = abc;
    }

    

   


    public int getAcceptMilestoneId() {
        return acceptMilestoneId;
    }

    public void setAcceptMilestoneId(int acceptMilestoneId) {
        this.acceptMilestoneId = acceptMilestoneId;
    }

    public int getDeclineMilestoneId() {
        return declineMilestoneId;
    }

    public void setDeclineMilestoneId(int declineMilestoneId) {
        this.declineMilestoneId = declineMilestoneId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }
    
    
    
}

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
@Table(name="workflow")
public class Workflow {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String workflowName;
    @Column(name="description")
    private String workflowDescription;
    @Transient
    private List<Milestone> wrokFlowSequence;

    public String getWorkflowDescription() {
        return workflowDescription;
    }

    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    public List<Milestone> getWrokFlowSequence() {
        return wrokFlowSequence;
    }

    public void setWrokFlowSequence(List<Milestone> wrokFlowSequence) {
        this.wrokFlowSequence = wrokFlowSequence;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    
    
}

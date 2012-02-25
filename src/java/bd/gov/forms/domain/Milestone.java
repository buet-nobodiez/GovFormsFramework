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
@Table(name="milestone")
public class Milestone {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="milestone_name")
    private String milestoneName;
    @Column(name="milestone_description")
    private String milestoneDescription;
    @Column(name="ministry_name")
    private String ministryName;
    @Column(name="role")
    private int role;
    @Transient
    private Milestone acceptValue;
    @Transient
    private Milestone declineValue;
    @Transient
    private int mileStoneFlag;
    

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public void setMilestoneDescription(String milestoneDescription) {
        this.milestoneDescription = milestoneDescription;
    }

    public String getMinistryName() {
        return ministryName;
    }

    public void setMinistryName(String ministryName) {
        this.ministryName = ministryName;
    }

    public Milestone getAcceptValue() {
        return acceptValue;
    }

    public void setAcceptValue(Milestone acceptValue) {
        this.acceptValue = acceptValue;
    }

    public Milestone getDeclineValue() {
        return declineValue;
    }

    public void setDeclineValue(Milestone declineValue) {
        this.declineValue = declineValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMileStoneFlag() {
        return mileStoneFlag;
    }

    public void setMileStoneFlag(int mileStoneFlag) {
        this.mileStoneFlag = mileStoneFlag;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
       
}

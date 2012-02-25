/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bd.gov.forms.domain;

import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author humayun
 */
@Entity
@Table(name="workflow_history")
public class WorkFlowHistory {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="track_id")
    private String trackId;
    @Column(name="user_id")
    private int userId;
    @Column(name="comment")
    private String comment;
    @Column(name="milestone_id")
    private int milestoneId;
    @Column(name="milestone_flag")
    private int milestoneFlag;
    @Column(name="date")
    private Date date;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMilestoneFlag() {
        return milestoneFlag;
    }

    public void setMilestoneFlag(int milestoneFlag) {
        this.milestoneFlag = milestoneFlag;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    
    
}

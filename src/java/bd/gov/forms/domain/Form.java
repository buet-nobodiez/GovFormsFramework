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
package bd.gov.forms.domain;

import java.util.List;
import javax.persistence.*;

/**
 * @author asif
 */
@Entity
@Table(name="form")
public class Form {
    
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="form_id")
    private String formId;
    @Column(name="title")
    private String title;
    @Column(name="subtitle")
    private String subTitle;
    @Column(name="detail")
    private String detail;
    @Column(name="table_name")
    private String tableName;
    @Column(name="status")
    private int status;
    @Lob
    @Column(name="template_file")
    private byte[] pdfTemplate;
    @Column(name="template_file_name")
    private String templateFileName;
    @Lob
    @Column(name="logo_file")
    private byte[] logoTemplate;
    @Column(name="logo_file_name")
    private String logoFileName;
    @Column(name="ministry")
    private String ministry;
    @Column(name="parent")
    private String parent;
    @Column(name="workflow_id")
    private int workflowId;

    
    @Transient
    private List<Field> fields;
    @Transient
    private String entryId;
    @Transient
    private String entryStatus;
    @Transient
    private int totalPages;

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }
    
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    public byte[] getLogoTemplate() {
        return logoTemplate;
    }

    public void setLogoTemplate(byte[] logoTemplate) {
        this.logoTemplate = logoTemplate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
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

    public byte[] getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(byte[] pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
        this.entryStatus = entryStatus;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getStatusStr() {
        switch (getStatus()) {
            case 1:
                return "Draft";
            case 2:
                return "Activated";
            case 3:
                return "Deactivated";
            default:
                return "";
        }
    }

}

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
package bd.gov.forms.web;

import bd.gov.forms.dao.FormDao;
import bd.gov.forms.dao.ListDao;
import bd.gov.forms.dao.UserDao;
import bd.gov.forms.domain.*;
import bd.gov.forms.utils.FormUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bd.gov.forms.utils.UserAccessChecker;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import pdf.TextFields;



import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.RadioCheckField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.Phrase;



// handling buttons
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCellEvent;
import java.awt.Color;
import java.io.FileOutputStream;



//for email
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;













/**
 * @author asif
 */
@Controller
@RequestMapping("/formBuilder")
@SuppressWarnings("unchecked")
public class FormBuilder {
    private static final Logger log = LoggerFactory.getLogger(FormBuilder.class);

    @Autowired
    private FormDao formDao;
    @Autowired
    private ListDao listDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    MessageSource messageSource;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "/newForm", method = RequestMethod.GET)
    public String newForm(ModelMap model, HttpServletRequest request) {
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form form = new Form();
        
        
        model.put("formDetailsCmd", form);        
        model.put("formAction", "saveForm");

        return "formDetails";
    }

    @RequestMapping(value = "/saveForm", method = RequestMethod.POST)
    public String saveForm(@ModelAttribute("formDetailsCmd") Form form, 
                           BindingResult result, HttpServletRequest request, ModelMap model) {

        model.put("formDetailsCmd", form);
        model.put("formAction", "saveForm");

        if (result.hasFieldErrors() || result.hasErrors()) {
            return "formDetails";
        }

        log.debug("form->save");

        form.setFormId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
        form.setStatus(1);
        
        User u = (User)RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
        form.setMinistry(u.getMinistryName());
        
        formDao.saveForm(form);

        model.put("message", "msg.form.submitted");
        model.put("msgType", "success");

        return "redirect:formList.htm";
    }

    @RequestMapping(value = "/editForm", method = RequestMethod.GET)
    public String editForm(@RequestParam(value = "formId", required = true) String formId,
                           ModelMap model, HttpServletRequest request) {
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form form = null;

        if (formId != null) {
            form = formDao.getForm(formId);
        }
        User user = (User) request.getSession().getAttribute("user");
        if(!form.getMinistry().equals(user.getMinistryName())) {
            return access;
        }

        if (form != null) {
            if (form.getTemplateFileName() != null && "".equals(form.getTemplateFileName().trim())) {
                form.setTemplateFileName(null);
            }
        }

        model.put("formDetailsCmd", form);
        model.put("formAction", "updateForm");

        return "formDetails";
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.POST)
    public String updateForm(@ModelAttribute("formDetailsCmd") Form form,
                             BindingResult result, HttpServletRequest request, ModelMap model) {

        model.put("formDetailsCmd", form);
        model.put("formAction", "updateForm");

        if (result.hasFieldErrors() || result.hasErrors()) {
            return "formDetails";
        }

        log.debug("form->update");
        log.debug("file size: {}", form.getPdfTemplate().length);

        formDao.updateForm(form);

        model.put("message", "msg.form.updated");
        model.put("msgType", "success");

        return "redirect:formList.htm";
    }

    @RequestMapping(value = "/dloadTemplate", method = RequestMethod.GET)
    public void pdfTemplate(@RequestParam(value = "formId", required = true) String formId,
                            ModelMap model, HttpServletResponse response) throws IOException {
        //response.setContentType("application/pdf");
        //TODO: file name 

        byte[] fileContent = formDao.getTemplateContent(formId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(fileContent);

        OutputStream os = response.getOutputStream();

        out.writeTo(os);
        os.flush();
    }

    @RequestMapping(value = "/removeTemplate", method = RequestMethod.GET)
    public String removeTemplate(@RequestParam(value = "formId", required = true) String formId,
                                 ModelMap model, HttpServletResponse response) throws IOException {
        formDao.removeTemplate(formId);
        return "redirect:editForm.htm?formId=" + formId;
    }
    
    @RequestMapping(value = "/removeLogoTemplate", method = RequestMethod.GET)
    public String removeLogoTemplate(@RequestParam(value = "formId", required = true) String formId,
                                 ModelMap model, HttpServletResponse response) throws IOException {
        formDao.removeLogoTemplate(formId);
        return "redirect:editForm.htm?formId=" + formId;
    }

    @RequestMapping(value = "/markChecked", method = RequestMethod.GET)
    public String markChecked(@RequestParam(value = "formId", required = true) String formId,
                              @RequestParam(value = "entryId", required = true) String entryId,
                              @RequestParam(value = "page", required = true) Integer page,
                              @RequestParam(value = "colName", required = true) String colName,
                              @RequestParam(value = "colVal", required = true) String colVal,
                              @RequestParam(value = "sortCol", required = false) String sortCol,
                              @RequestParam(value = "sortDir", required = false) String sortDir,
                              @RequestParam(value = "checked", required = true) boolean checked,
                              ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form role = formDao.getForm(formId);
        String status = checked ? "Checked" : "Submitted";
        formDao.updateEntryStatus(role, entryId, status);
        
        /*******/
        if("Submitted".equals(role.getStatus())) {
            int firstMilestoneId = Integer.parseInt(formDao.getFirstMilestoneId(role.getWorkflowId()).toString());
            formDao.updateTrackMilestone(firstMilestoneId, entryId);
        }
        
        /****/

        return "redirect:entryList.htm?formId=" + FormUtil.formatValue(formId) + "&page=" + FormUtil.formatValue(page)
                + "&colName=" + FormUtil.formatValue(colName) + "&colVal=" + FormUtil.formatValue(colVal)
                + "&sortCol=" + FormUtil.formatValue(sortCol) + "&sortDir=" + FormUtil.formatValue(sortDir);
    }

    public List getEntryListHeaders(Form form) {
        List list = new ArrayList();

        list.add("Date");
        list.add("Time");
        list.add("Status");
        for (Field field : form.getFields()) {
            list.add(field.getLabel());
        }

        return list;
    }

    @RequestMapping(value = "/excelExport", method = RequestMethod.GET)
    public String excelExport(@RequestParam(value = "formId", required = true) String formId,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "colName", required = false) String colName,
                              @RequestParam(value = "colVal", required = false) String colVal,
                              @RequestParam(value = "sortCol", required = false) String sortCol,
                              @RequestParam(value = "sortDir", required = false) String sortDir,
                              ModelMap model, HttpServletResponse response, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (page == null) {
            page = 1;
        }

        Form form = formDao.getFormWithFields(formId);

        List<HashMap> list = formDao.getEntryList(form, page, colName, colVal, sortCol, sortDir, false);
        List<String> headers = getEntryListHeaders(form);

        response.setContentType("application/vnd.ms-excel");
        //TODO: file name 

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Excel Report");

        int r = 0;
        HSSFRow row = sheet.createRow((short) r++);
        int count = 0;
        for (String header : headers) {
            HSSFCell cell = row.createCell(count++);
            cell.setCellValue(header);
        }

        for (HashMap hashmap : list) {
            row = sheet.createRow((short) r++);
            count = 0;

            HSSFCell cell = row.createCell(count++);
            cell.setCellValue((String) hashmap.get("entry_date"));

            cell = row.createCell(count++);
            cell.setCellValue((String) hashmap.get("entry_time"));

            cell = row.createCell(count++);
            cell.setCellValue((String) hashmap.get("entry_status"));

            for (Field field : form.getFields()) {
                cell = row.createCell(count++);
                cell.setCellValue((String) hashmap.get(field.getColName()));
            }
        }

        String fileName = "Report-" + formId + ".xls";
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        response.setContentType("application/vnd.ms-excel");

        ServletOutputStream outputStream = response.getOutputStream();
        sheet.getWorkbook().write(outputStream);
        outputStream.flush();

        return null;
    }

    @RequestMapping(value = "/printHtml", method = RequestMethod.GET)
    public String printHtml(@RequestParam(value = "formId", required = true) String formId,
                            @RequestParam(value = "entryId", required = true) String entryId,
                            ModelMap model, HttpServletResponse response) throws IOException {

        byte[] fileContent = formDao.getTemplateContent(formId);
        Form form = formDao.getFormWithFields(formId);
        form.setEntryId(entryId);

        form = formDao.getEntry(form);
        String report = "";

        if (fileContent != null) {
            report = new String(fileContent, "UTF8");

            for (Field field : form.getFields()) {
                if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
                    report = report.replaceAll("#" + field.getColName() + ":label#", field.getLabel());
                    report = report.replaceAll("#" + field.getColName() + ":value#", field.getStrVal());
                }
            }
        } else {
            report += "<table cellspacing='0' cellpadding='0' style='border:1px solid #aaa;width:98%;'>";

            for (Field field : form.getFields()) {
                if (fieldTypeIsNotOfFileOrNoteOrSection(field)) {
                    report += "<tr>";
                    report += "<td>";
                    report += field.getLabel();
                    report += "</td>";
                    report += "<td>";
                    report += field.getStrVal();
                    report += "</td>";

                    report += "</tr>";

                }
            }

            report += "</table>";
        }

        model.put("report", report);

        return "formReport";
    }

    private boolean fieldTypeIsNotOfFileOrNoteOrSection(Field field) {
        return !"file".equals(field.getType()) && !"note".equals(field.getType()) && !"section".equals(field.getType());
    }

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(@RequestParam(value = "formId", required = true) String formId,
                           ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form form = formDao.getForm(formId);

        if (form.getStatus() == 1) {
            formDao.initDbIdentifiers(form.getId());
            form = formDao.getFormWithFields(formId);
            formDao.createTable(form);
        }

        formDao.updateStatus(formId, 2);

        return "redirect:formList.htm";
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.GET)
    public String deactivate(@RequestParam(value = "formId", required = true) String formId,
                             ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        formDao.updateStatus(formId, 3);

        return "redirect:formList.htm";
    }

    @RequestMapping(value = "/deleteForm", method = RequestMethod.GET)
    public String deleteForm(@RequestParam(value = "formId", required = true) String formId,
                             ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        formDao.deleteForm(formId);

        return "redirect:formList.htm";
    }

    @RequestMapping(value = "/deleteField", method = RequestMethod.GET)
    public String deleteField(@RequestParam(value = "fieldId", required = true) String fieldId,
                              @RequestParam(value = "formId", required = true) String formId,
                              ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        log.debug("formBuilder->deleteField");

        Form form = formDao.getForm(formId);

        if (form.getStatus() > 1) {
            model.put("message", "field.delete.failed");
            model.put("msgType", "failed");
            return "redirect:formList.htm";
        }

        Field field = formDao.getField(fieldId);
        formDao.deleteField(fieldId, form.getId());
        formDao.updateOrder(field.getFormId(), field.getFieldOrder(), "-");

        return "redirect:design.htm?formId=" + formId;
    }

    @RequestMapping(value = "/moveField", method = RequestMethod.GET)
    public String moveField(@RequestParam(value = "fieldId", required = true) String fieldId,
                            @RequestParam(value = "formId", required = true) String formId,
                            @RequestParam(value = "order", required = true) int order,
                            ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        log.debug("formBuilder->moveField. fieldId: {}", fieldId);

        Field field = formDao.getField(fieldId);

        formDao.moveField(field.getFormId(), field.getFieldId(), field.getFieldOrder(), order);

        return "redirect:design.htm?formId=" + formId;
    }

    @RequestMapping(value = "/newField", method = RequestMethod.GET)
    public String newField(@RequestParam(value = "formId", required = true) String formId,
                           @RequestParam(value = "type", required = true) String type,
                           @RequestParam(value = "order", required = true) int order,
                           ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form form = formDao.getForm(formId);
        if (form.getStatus() > 1) {
            model.put("message", "field.add.failed");
            model.put("msgType", "failed");

            return "redirect:formList.htm";
        }

        Field field = new Field();
        field.setFieldOrder(order);
        field.setType(type);    //TODO: invalid type
        field.setFormIdStr(formId);
        field.setRequired(0);

        model.put("fieldCmd", field);
        model.put("formAction", "saveField");

        return "field";
    }

    @RequestMapping(value = "/saveField", method = RequestMethod.POST)
    public String saveField(@ModelAttribute("fieldCmd") Field field,
                            BindingResult result,
                            HttpServletRequest request,
                            ModelMap model) {

        model.put("fieldCmd", field);
        model.put("formAction", "saveField");

        if (result.hasFieldErrors() || result.hasErrors()) {
            return "field";
        }

        log.debug("field->save");

        Form frm = formDao.getForm(field.getFormIdStr());
        field.setFormId(frm.getId());
        field.setFieldId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));

        formDao.updateOrder(field.getFormId(), field.getFieldOrder(), "+");
        formDao.saveField(field);

        model.put("message", "msg.field.submitted");
        model.put("msgType", "success");

        return "redirect:design.htm?formId=" + field.getFormIdStr();
    }

    @RequestMapping(value = "/editField", method = RequestMethod.GET)
    public String editField(@RequestParam(value = "formId", required = true) String formId,
                            @RequestParam(value = "fieldId", required = true) String fieldId,
                            ModelMap model, HttpServletRequest request) {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Field field = null;

        if (fieldId != null) {
            field = formDao.getField(fieldId);
        }

        if (field != null) {
            field.setFormIdStr(formId);
        }

        model.put("fieldCmd", field);
        model.put("formAction", "updateField");

        return "field";
    }

    @RequestMapping(value = "/updateField", method = RequestMethod.POST)
    public String updateField(@ModelAttribute("fieldCmd") Field field,
                              BindingResult result, HttpServletRequest request, ModelMap model) {

        model.put("fieldCmd", field);
        model.put("formAction", "updateField");

        if (result.hasFieldErrors() || result.hasErrors()) {
            return "field";
        }

        log.debug("field->update");

        Form form = formDao.getForm(field.getFormIdStr());
        field.setFormId(form.getId());
        formDao.updateField(field);

        model.put("message", "msg.field.updated");
        model.put("msgType", "success");

        return "redirect:design.htm?formId=" + field.getFormIdStr();
    }

    @RequestMapping(value = "/formList", method = RequestMethod.GET)
    public String formList(@RequestParam(value = "page", required = false) Integer page,
                           ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (page == null) {
            page = 1;
        }
        User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_SESSION);
        List list = formDao.getFormList(page, user.getMinistryName());
        model.put("forms", list);

        return "formList";
    }
    
    @RequestMapping(value="/previousVersions", method= RequestMethod.GET)
    public String previousVersions(@RequestParam(value = "formId", required = true) String formId,
                           ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        List formList = formDao.getParents(formId);
        model.put("parents", formList);
        return "formVersions";
    }

    @RequestMapping(value = "/formInfo", method = RequestMethod.GET)
    public String formInfo(@RequestParam(value = "formId", required = true) String formId,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        
        Form form = formDao.getFormWithFields(formId);
        User user = (User) request.getSession().getAttribute("user");
        if(!form.getMinistry().equals(user.getMinistryName())) {
            return access;
        }

        
        model.put("form", form);

        return "formInfo";
    }
    
    @RequestMapping(value="/newVersion", method = RequestMethod.GET)
    public String newVersion(@RequestParam(value = "formId", required = true) String formId,
                         ModelMap model, HttpServletRequest request) throws Exception {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form form = null;

        if (formId != null) {
            form = formDao.getForm(formId);
        }
        User user = (User) request.getSession().getAttribute("user");
        if(!form.getMinistry().equals(user.getMinistryName())) {
            return access;
        }

        if (form != null) {
            if (form.getTemplateFileName() != null && "".equals(form.getTemplateFileName().trim())) {
                form.setTemplateFileName(null);
            }
        }

        model.put("formDetailsCmd", form);
        model.put("formAction", "version");


        return "formDetails";
        
    }
    
    @RequestMapping(value="/version", method= RequestMethod.POST)
    public String version(@ModelAttribute("formDetailsCmd") Form formv,
                             BindingResult result, HttpServletRequest request, ModelMap model) {

        model.put("formDetailsCmd", formv);
        model.put("formAction", "updateForm");

        if (result.hasFieldErrors() || result.hasErrors()) {
            return "formDetails";
        }

        log.debug("form->update");
        log.debug("file size: {}", formv.getPdfTemplate().length);

        //formDao.updateForm(form);
        //Id form new version form
        String id = Long.toString(System.nanoTime()) + new Long(new Random().nextLong());
        Form form = formDao.getFormWithFields(formv.getFormId());
        
        Form versionForm = new Form();
        versionForm.setFormId(id);
        versionForm.setStatus(1);
        versionForm.setTitle(formv.getTitle());
        versionForm.setSubTitle(formv.getSubTitle());
        versionForm.setDetail(formv.getDetail());
        versionForm.setMinistry(form.getMinistry());
        versionForm.setPdfTemplate(form.getPdfTemplate());
        versionForm.setTemplateFileName(form.getTemplateFileName());
        versionForm.setLogoTemplate(form.getLogoTemplate());
        versionForm.setLogoFileName(form.getLogoFileName());
        versionForm.setParent(form.getFormId());
        
        formDao.saveForm(versionForm);
        
        Form getVersionForm = formDao.getForm(id);
        
        
        
        for(Field field : form.getFields()) {
            Field copyField = new Field();
            copyField.setFormId(getVersionForm.getId());
            copyField.setFieldId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
            
            copyField.setType(field.getType());
            copyField.setInputType(field.getInputType());
            copyField.setColName(field.getColName());
            copyField.setLabel(field.getLabel());
            copyField.setHelpText(field.getHelpText());
            copyField.setOptions(field.getOptions());
            copyField.setListDataId(field.getListDataId());
            copyField.setDefaultValue(field.getDefaultValue());
            copyField.setFieldOrder(field.getFieldOrder());
            copyField.setRequired(field.getRequired());
            
            formDao.saveField(copyField);
        }
        
        
        
        model.put("message", "msg.form.updated");
        model.put("msgType", "success");
        return "redirect:formList.htm";
    }
    
    @RequestMapping(value = "/design", method = RequestMethod.GET)
    public String design(@RequestParam(value = "formId", required = true) String formId,
                         ModelMap model, HttpServletRequest request) throws Exception {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Form frm = null;

        if (formId != null) {
            frm = formDao.getFormWithFields(formId);
        }
        
        User user = (User) request.getSession().getAttribute("user");
        if(!frm.getMinistry().equals(user.getMinistryName())) {
            return access;
        }

        if (frm != null) {
            if (frm.getStatus() > 1) {
                model.put("doneMessage", "msg.access.denied");
                model.put("doneMsgType", "failed");

                return "redirect:done.htm";
            }
            initForm(frm);
        }

        model.put("formCmd", frm);
        model.put("formAction", "formDesign");

        return "form";
    }

    @RequestMapping(value = "/formDesign", method = RequestMethod.POST)
    public String formDesign(@ModelAttribute("fieldCmd") Form form,
                             BindingResult result, HttpServletRequest request, ModelMap model) {

        model.put("message", "msg.form.updated");   //TODO: Update msg
        model.put("msgType", "success");            //TODO: Update msg

        return "redirect:design.htm?formId=" + form.getFormId();
    }

    @RequestMapping(value = "/newEntry", method = RequestMethod.GET)
    public String newEntry(@RequestParam(value = "formId", required = true) String formId, ModelMap model)
            throws Exception {
        
        
        
        Form form = null;

        if (formId != null) {
            form = formDao.getFormWithFields(formId);
        }

        if (form != null) {
            if (form.getStatus() != 2) {//2-active, 3-deactive
                model.put("doneMessage", "msg.access.denied");
                model.put("doneMsgType", "failed");

                return "redirect:done.htm";
            }
            initForm(form);
        }

        model.put("formCmd", form);
        model.put("formAction", "saveEntry");

        return "form";
    }
    
    
    
    
    
    
    
    
    

    @RequestMapping(value = "/saveEntry", method = RequestMethod.POST)
    public String saveEntry(@ModelAttribute("formCmd") Form formCmd,
                            BindingResult result, HttpServletRequest request, ModelMap model) {

        log.debug("form->save. formId: {}", formCmd.getFormId());

        Form formDb = formDao.getFormWithFields(formCmd.getFormId());

        //formDb.setEntryId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
        formDb.setEntryId(Long.toString(System.nanoTime()));
        formDb.setEntryStatus("Submitted");

        if (formDb.getFields() != null && formDb.getFields().size() > 0) {
            for (int i = 0; i < formDb.getFields().size(); i++) {
                Field fldCmd = formCmd.getFields().get(i);
                Field fldDb = formDb.getFields().get(i);

                if (fldCmd.getFieldId().equals(fldDb.getFieldId())) {
                    fldDb.setByteVal(fldCmd.getByteVal());
                    fldDb.setStrVal(fldCmd.getStrVal());
                } else {
                    throw new RuntimeException("Field IDs do not match");
                }

                if (fldDb.getRequired() == 1 && fldDb.getByteVal() == null && fldDb.getStrVal() == null) {
                    throw new RuntimeException("Required value not found.");
                }
            }
        }

        formDao.saveEntry(formDb);
        
        User user = (User) request.getSession().getAttribute("user");
        
        Track track = new Track();
        track.setFormId(formDb.getFormId());
        track.setTrackId(formDb.getEntryId());
        track.setUserId(user.getId());
        track.setFormName(formDb.getTitle());
        
        formDao.saveTrack(track);
        
        model.put("doneMessage", "msg.form.submitted");
        model.put("doneMsgType", "success");
        model.put("trackId", formDb.getEntryId());

        return "redirect:done.htm";
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String done(@RequestParam(value = "doneMessage", required = true) String message,
                       @RequestParam(value = "doneMsgType", required = true) String msgType,
                       @RequestParam(value = "trackId", required = false) String trackId,
                       ModelMap model) throws Exception {

        model.put("doneMessage", message);
        model.put("doneMsgType", msgType);
        model.put("trackId", trackId);

        return "done";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) throws Exception {
        log.info("this is index message\n\n\n\n\n\n\n\n\nIndex Message Here");
        List list = formDao.getPublicForms();
        
        model.put("forms", list);

        return "index";
    }

    @RequestMapping(value = "/entryList", method = RequestMethod.GET)
    public String entryList(@RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "formId", required = true) String formId,
                            @RequestParam(value = "colName", required = false) String colName,
                            @RequestParam(value = "colVal", required = false) String colVal,
                            @RequestParam(value = "sortCol", required = false) String sortCol,
                            @RequestParam(value = "sortDir", required = false) String sortDir,
                            ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (page == null) {
            page = 1;
        }

        if (sortDir == null || "".equals(sortDir)) {
            sortDir = "DESC";
        }
        if (!"ASC".equals(sortDir) && !"DESC".equals(sortDir)) {
            sortDir = "DESC";
        }
        log.debug("sort order: {}", sortDir);

        Form form = formDao.getFormWithFields(formId);
        User user = (User) request.getSession().getAttribute("user");
        if(!form.getMinistry().equals(user.getMinistryName())) {
            return access;
        }
        
        int totalCount=formDao.getFormEntryCount(form,null);
        int checkedCount=formDao.getFormEntryCount(form,"Checked");
        int uncheckedCount=totalCount-checkedCount;
        model.put("totalCount", totalCount);
        model.put("checkedCount", checkedCount);
        model.put("uncheckedCount", uncheckedCount);

        List list = formDao.getEntryList(form, page, colName, colVal, sortCol, sortDir, true);

        model.put("entries", list);
        model.put("headers", getEntryListHeaders(form));
        model.put("form", form);
        model.put("formId", formId);
        model.put("page", page);
        model.put("colName", colName);
        model.put("colVal", colVal);
        model.put("sortCol", sortCol);
        model.put("sortDirX", sortDir);

        if ("ASC".equals(sortDir)) {
            sortDir = "DESC";
        } else {
            sortDir = "ASC";
        }
        log.debug("sort order: {}", sortDir);

        model.put("sortDir", sortDir);
        model.put("totalPages", form.getTotalPages());

        return "entryList";
    }

    private void initForm(Form form) throws Exception {
        for (Field field : form.getFields()) {
            String css = "";
            if (field.getRequired() == 1) {
                css += "required";
            }
            if (field.getInputType() != null) {
                css += " " + field.getInputType();
            }
            field.setCssClass(css);

            if (field.getListDataId() != 0) {
                String sysId = listDao.getListDataSysId(field.getListDataId());
                ListData lst = listDao.getListData(sysId);
                List list = lst.getList(field.getType());
                field.setList(list);
            }
        }
    }

    @ModelAttribute("inputType")
    public Map getInputType() {
        Map fieldType = new HashMap();
        fieldType.put("", "");
        fieldType.put("number", "Number");
        fieldType.put("currency", "Currency");
        fieldType.put("date", "Date");
        fieldType.put("email", "E-mail");

        return fieldType;
    }

    @ModelAttribute("yesNoOption")
    public Map getYesNoOption(HttpServletRequest request, Locale locale) {
        Map options = new HashMap();

        options.put("1", messageSource.getMessage("yes", null, locale));
        options.put("0", messageSource.getMessage("no", null, locale));

        return options;
    }

    @ModelAttribute("listSrc")
    public Map getListSrc() {
        Map map = new LinkedHashMap();

        List<ListData> list = listDao.getListDataList();

        for (ListData listData : list) {
            map.put(listData.getId(), listData.getName());
        }

        return map;
    }
    
    @ModelAttribute("milestoneOption")
    public Map getMilestoneOption() {
        Map m = new HashMap();
        
        List<Milestone> milestoneList = userDao.getMilestoneList();
        
        for(Milestone milestone : milestoneList) {
            m.put(milestone.getId(), milestone.getMilestoneName());
        }
        
        return m;
    }
    
    @ModelAttribute("workflowOption")
    public Map getWorkflowOption() {
        Map m = new HashMap();
        
        List<Workflow> workflowList = userDao.getWorkflowList();
        
        for(Workflow workflow : workflowList) {
            m.put(workflow.getId(), workflow.getWorkflowName());
        }
        
        return m;
    }
    
    
    
    
    @RequestMapping(value="/dashboard")
    public String dashboard(ModelMap model, HttpServletRequest request)throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        
        if(user.getAdmin() != 0) {
            String ministryName = user.getMinistryName();
            int role = user.getAdmin();

            List<Milestone> milestoneList = userDao.getMilestoneList(ministryName, role);
            List<Track> trackList = new ArrayList<Track>();

            for (Milestone milestone : milestoneList) {
                List<Track> a = userDao.getTrackList(milestone.getId());

                for (Track t : a) {
                    WorkFlowHistory history = userDao.getWorkflowHistory(t.getTrackId());
                    t.setWorkflowHistory(history);
                    Milestone mile = userDao.getMilestone(history.getMilestoneId());
                    t.setMilestone(mile);
                    trackList.add(t);
                }
            }
            model.put("trackList", trackList);
        } else {
            List trackList = userDao.getUserTrackList(user.getId());
            model.put("trackList", trackList);
        }
        
        
        return "dashboard";
    }

    @RequestMapping(value = "/saveHistory", method = RequestMethod.POST)
    public String entryList(@RequestParam(value = "trackId", required = false) String trackId,
                            @RequestParam(value = "milestoneId", required = false) int milestoneId,
                            @RequestParam(value = "button", required = false) String buttonValue,
                            @RequestParam(value = "comment", required = false) String comment,
                            @RequestParam(value = "formId", required = false) String formId,
                            ModelMap model, HttpServletRequest request) throws IOException {
    
        int milestoneFlag = 0;
        if("Approve".equals(buttonValue) ) {
            milestoneFlag = 1;
        }
        if("Decline".equals(buttonValue) ) {
            milestoneFlag = 4;
        }
        
        int workflowId = Integer.parseInt(userDao.getWorkflowId(formId).toString());
        int nextMilestoneId = Integer.parseInt(userDao.getnextMilestoneId(workflowId, milestoneId, milestoneFlag).toString());
        
        User user = (User) request.getSession().getAttribute("user");
        
        WorkFlowHistory history = new WorkFlowHistory();
        history.setComment(comment);
        history.setMilestoneFlag(milestoneFlag);
        history.setMilestoneId(milestoneId);
        history.setTrackId(trackId);
        history.setDate(new Date());
        history.setUserId(user.getId());
        
        userDao.saveWorkflowDetailHistory(history);
        formDao.updateTrackMilestone(nextMilestoneId, trackId);
        
        return "dashboard";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @RequestMapping(value = "/individualpdf", method = RequestMethod.GET)
    public String individualpdf(@RequestParam(value = "formId", required = true) String formId,
                            @RequestParam(value = "entryId", required = true) String entryId,
                            ModelMap model, HttpServletResponse response) throws IOException {
        
        
        byte[] fileContent = formDao.getTemplateContent(formId);
        Form form = formDao.getFormWithFields(formId);
        form.setEntryId(entryId);

        form = formDao.getEntry(form);
        String report = "";
        
        Document document = new Document();        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        

        if (fileContent != null) 
        {
            report = new String(fileContent, "UTF8");

            for (Field field : form.getFields()) 
            {
                if (fieldTypeIsNotOfFileOrNoteOrSection(field)) 
                {
                    report = report.replaceAll("#" + field.getColName() + ":label#", field.getLabel());
                    report = report.replaceAll("#" + field.getColName() + ":value#", field.getStrVal());
                }
            }
        } 
        
        else 
        {
             // step 2
        try{            
            response.reset();  
            response.setContentType("application/pdf");  
            response.setHeader("Content-disposition","inline; filename=test.pdf");  

            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            response.setHeader("Pragma", "No-cache");
            
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // step 3
            document.open();
            
            PdfPCell space;
            space = new PdfPCell();
            space.setBorder(Rectangle.NO_BORDER);
            space.setColspan(2);
            space.setFixedHeight(8) ;
                
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell; 
        
            
            
            
            
            
            report += "<table cellspacing='0' cellpadding='0' style='border:1px solid #aaa;width:98%;'>";

            for (Field field : form.getFields()) 
            {
                if (fieldTypeIsNotOfFileOrNoteOrSection(field)) 
                {
                    
                    report += field.getLabel();
                    
                    report += field.getStrVal();
                    
                    
                    table.setWidths(new int[]{ 1, 2 });
                    table.addCell(field.getLabel());
                    //cell = new PdfPCell();
                    //cell.setCellEvent(new TextFields(1));
                    table.addCell(field.getStrVal());

                }
            }
            
            document.add(table);           
            document.close();           
            ServletOutputStream out = response.getOutputStream();        
            baos.writeTo(out);
            out.flush();
            
                  
        }
        
        catch(Exception ex)
        {
                System.out.println("Could not print reasone::"+ex.toString());

        }
        
        
        
    }
        
        return null;
        
    }
    
    
    

    
    class FieldCell implements PdfPCellEvent{
		
		PdfFormField formField;
		PdfWriter writer;
		int width;
		
		public FieldCell(PdfFormField formField, int width, 
			PdfWriter writer){
			this.formField = formField;
			this.width = width;
			this.writer = writer;
		}
		
                public void cellLayout(PdfPCell cell, Rectangle rect, 
			PdfContentByte[] canvas){
			try{
				// delete cell border
				PdfContentByte cb = canvas[PdfPTable
					.LINECANVAS];
				cb.reset();
				
				formField.setWidget(
					new Rectangle(50, 
						50, 
						70+width, 
						50), 
						PdfAnnotation
						.HIGHLIGHT_NONE);
				
                                System.out.println("hgshsgdhsgdhsd");
				writer.addAnnotation(formField);
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
	}
    
    
    @RequestMapping(value = "/pdfExport", method = RequestMethod.GET)
    public String pdfExport(@RequestParam(value = "formId", required = true) String formId,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "colName", required = false) String colName,
                              @RequestParam(value = "colVal", required = false) String colVal,
                              @RequestParam(value = "sortCol", required = false) String sortCol,
                              @RequestParam(value = "sortDir", required = false) String sortDir,
                              ModelMap model, HttpServletResponse response, HttpServletRequest request) throws IOException {
        
        
        
        
        Document document = new Document();        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // step 2
        try{            
            response.reset();  
            response.setContentType("application/pdf");  
            response.setHeader("Content-disposition","inline; filename=test.pdf");  

            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            response.setHeader("Pragma", "No-cache");
            
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // step 3
            document.open();
    
            Form form = null;
            //System.out.println("The form id is 1:"+formId);
            if (formId != null) 
            {
                form = formDao.getFormWithFields(formId);
            }

            if (form != null) 
            {
                if (form.getStatus() != 2) {//2-active, 3-deactive
                    model.put("doneMessage", "msg.access.denied");
                    model.put("doneMsgType", "failed");

                    return "redirect:done.htm";
                }
                initForm(form);
            }
        
            List<Field> fieldList = form.getFields();
        
            if(fieldList.isEmpty())
            {       
                System.out.println("The list size is zero");
            }
            
            PdfPCell space;
            space = new PdfPCell();
            space.setBorder(Rectangle.NO_BORDER);
            space.setColspan(2);
            space.setFixedHeight(8) ;
                
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell; 
        
      
             //PdfPCell cell;                
            table.setWidths(new int[]{ 1, 2 });
                    
            int i=0;
            for(Field f : fieldList)
            {       
                if( "text".equals(f.getType()))
                {            
                   

                    table.addCell(f.getLabel());
                    cell = new PdfPCell();
                    cell.setCellEvent(new TextFields(1,i));
                    table.addCell(cell);

                }
                else if( "textarea".equals(f.getType()) )
                {
                    table.addCell(f.getLabel());
                    cell = new PdfPCell();
                    cell.setCellEvent(new TextFields(1,i));
                    cell.setFixedHeight(60);
                    table.addCell(cell);

                }
                else if( "select".equals(f.getType()) )
                {
                    table.addCell(f.getType());
                    cell = new PdfPCell();
                    cell.setCellEvent(new ChoiceFields(3 , f.getList().toArray() )  );
                    table.addCell(cell);
                    //table.addCell(space);
                    System.out.println("ajsdhd");
                }
                i++;
            }
   	
		
          
          /*
            for(Field f : fieldList)
            {  
                
                if( "radio".equals(f.getType()) )
                {    
                    System.out.println("List "+f.getList()+"  Oppt"+f.getOptions()+ "    df"+f.getColName());
                    
                    writer = PdfWriter.getInstance(document, new FileOutputStream("TextFieldForm.pdf"));
                 
                    //writer.addJavaScript(Utilities.readFileToString(""));
                    // add the radio buttons
                    PdfContentByte canvas = writer.getDirectContent();
                    Font font = new Font(FontFamily.HELVETICA, 14);
                    Rectangle rect;
                    PdfFormField field;
                    PdfFormField radiogroup = PdfFormField.createRadioButton(writer, true);
                    radiogroup.setFieldName("language");
                    RadioCheckField radio;
                    for (int i = 0; i < 2; i++) 
                    {
                        rect = new Rectangle(40, 806 - i * 40, 60, 788 - i * 40);
                        radio = new RadioCheckField(writer, rect, null, f.getLabel());
                        radio.setBorderColor(GrayColor.GRAYBLACK);
                        radio.setBackgroundColor(GrayColor.GRAYWHITE);
                        radio.setCheckType(RadioCheckField.TYPE_CIRCLE);
                        field = radio.getRadioField();
                        radiogroup.addKid(field);
                        
                        writer.addAnnotation(field);
                       
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                            new Phrase(f.getLabel(), font), 70, 790 - i * 40, 0);
                    }
                    //table.addCell(f.getLabel());
                    //cell = new PdfPCell();
                    
                    //document.add(radiogroup);
                    //writer.addAnnotation(radiogroup);
                    

                }
                
            }       */
          
            
            
                  // Add submit button	
		PushbuttonField submitBtn = new PushbuttonField(writer,
				new Rectangle(400,700,370,670),"submitPOST");
		//submitBtn.setBackgroundColor(Color.GRAY);
		submitBtn.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
		submitBtn.setText("Submit");
		submitBtn.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		PdfFormField submitField = submitBtn.getField();
		submitField.setAction(PdfAction.createSubmitForm
                        ("http://localhost:8084/GovForm-07-02/formBuilder/pdfresponse.htm",
                        null, PdfAction.SUBMIT_HTML_FORMAT));
		
		writer.addAnnotation(submitField);
                
                
                
                
		
            
            document.add(table);
            
            
            System.out.println("Pdf creation successful");
        
 
            
            document.close();
            
            ServletOutputStream out = response.getOutputStream();        
            baos.writeTo(out);
            out.flush();

            }
            catch(Exception ex){
                System.out.println("Could not print reasone::"+ex.toString());

            }
        
        
      //////////////////////////////////////// email part////////////////////////////  
        //email functionalities
        // Recipient's email ID needs to be mentioned.
      String to = "tanviranik@gmail.com";

      // Sender's email ID needs to be mentioned
      String from = "tanvir_cse@yahoo.com";

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("This is the Subject Line!");

         // Now set the actual message
         message.setText("This is actual message");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
        //////////////////////////////////////// email part////////////////////////////
        
        return null;
        
    }
    
    //pdf response 
    
    @RequestMapping(value = "/pdfresponse", method = RequestMethod.POST)
    public String pdfresponse( @RequestParam(value = "text_0") String value1,
            ModelMap model, HttpServletResponse response, HttpServletRequest request) throws IOException {
        
        System.out.println("Pdf DONEEEEEEEEEEE  "+value1);
        System.out.println("Pdf DONEEEEEEEEEEE  "+request.getParameter("text_1"));
        System.out.println("Pdf DONEEEEEEEEEEE  "+request.getParameter("text_2"));
        System.out.println("Pdf DONEEEEEEEEEEE  "+request.getParameter("text_3"));


        return null;
    }
    
    
    
    
    
    
    
       class ChoiceFields implements PdfPCellEvent {
        
        protected int cf;
        public final String[] LANGUAGES; 
        public final String[] EXPORTVALUES ;
        
        public ChoiceFields(int cf , Object[] ob) 
        {
            this.cf = cf;
            LANGUAGES = new String[ob.length] ;
            EXPORTVALUES = new String[ob.length] ;
            for(int i=0; i<ob.length; i++)
            {
                LANGUAGES[i] = new String(ob[i].toString());
                EXPORTVALUES[i] = new String(""+i); ;
            }
        }
         
        
    
    public void cellLayout(PdfPCell cell, Rectangle rectangle,
            PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();
        TextField text = new TextField(writer, rectangle,
                String.format("choice_%s", cf));
        try {
            switch(cf) {
            case 1:
                text.setChoices(LANGUAGES);
                text.setChoiceExports(EXPORTVALUES);
                text.setChoiceSelection(2);
                writer.addAnnotation(text.getListField());
                break;
            case 2:
                text.setChoices(LANGUAGES);
                text.setBorderColor(BaseColor.GREEN);
                text.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
                text.setOptions(TextField.MULTISELECT);
                ArrayList<Integer> selections = new ArrayList<Integer>();
                selections.add(0);
                selections.add(2);
                text.setChoiceSelections(selections);
                PdfFormField field = text.getListField();
                writer.addAnnotation(field);
                break;
            case 3:
                text.setBorderColor(BaseColor.RED);
                text.setBackgroundColor(BaseColor.GRAY);
                text.setChoices(LANGUAGES);
                text.setChoiceExports(EXPORTVALUES);
                text.setChoiceSelection(4);
                writer.addAnnotation(text.getComboField());
                break;
            case 4:
                text.setChoices(LANGUAGES);
                text.setOptions(TextField.EDIT);
                writer.addAnnotation(text.getComboField());
                break;
            }
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
    }
    
    

}

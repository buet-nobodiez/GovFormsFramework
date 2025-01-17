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

import bd.gov.forms.dao.UserDao;
import bd.gov.forms.domain.*;
import bd.gov.forms.utils.FormUtil;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bd.gov.forms.utils.UserAccessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author asif
 */
@Controller
@RequestMapping("/userMgt")
@SuppressWarnings("unchecked")
public class UserMgt {
    
    private static final Logger log = LoggerFactory.getLogger(UserMgt.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/newUser", method = RequestMethod.GET)
    public String newUser(ModelMap model, HttpServletRequest request) {
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        User user = new User();
        model.put("ministryList", userDao.getMinistryListString());
        model.put("userCmd", user);
        model.put("formAction", "saveUser");

        return "user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("userCmd") User user,
                           BindingResult result, HttpServletRequest request, ModelMap model) {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (FormUtil.isEmpty(user.getUserName()) || FormUtil.isEmpty(user.getPassword())) {
            throw new RuntimeException("Required value not found.");
        }
        if (userDao.getCountWithUserName(user.getUserName()) > 0) {
            throw new RuntimeException("User Name must be unique");
        }

        model.put("userCmd", user);
        model.put("formAction", "saveUser");

        log.debug("user->save");

        user.setSysId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
        User userSession = (User) request.getSession().getAttribute("user");
        //user.setMinistryName(userSession.getMinistryName());
        
        if(user.getAdmin() == 0) {
            user.setAdmin(10);
        }
        
        userDao.saveUser(user);

        model.put("message", "msg.form.submitted");
        model.put("msgType", "success");

        return "redirect:userList.htm";
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.GET)
    public String editUser(@RequestParam(value = "sysId", required = true) String sysId,
                           ModelMap model, HttpServletRequest request) {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        User user = sysId != null ? userDao.getUser(sysId) : null;

        model.put("userCmd", user);
        model.put("formAction", "updateUser");

        return "user";
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userCmd") User cmd,
                             BindingResult result, HttpServletRequest request, ModelMap model) {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (FormUtil.isEmpty(cmd.getUserName())) {
            throw new RuntimeException("Required value not found.");
        }

        model.put("userCmd", cmd);
        model.put("formAction", "updateUser");

        log.debug("user->update");
        
        User user = (User) request.getSession().getAttribute("user");
        cmd.setMinistryName(user.getMinistryName());
        userDao.updateUser(cmd);

        model.put("message", "msg.form.updated");
        model.put("msgType", "success");

        return "redirect:userList.htm";
    }

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public String userList(ModelMap model, HttpServletRequest request) throws IOException {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        
        User user = (User) request.getSession().getAttribute("user");
        List list;
        if(user.getAdmin()==2) {
            list = userDao.getUserList();
        }
        
        else{
            list = userDao.getUserList(user.getMinistryName());
        }
        
        //List list = userDao.getUserList(user.getMinistryName());
        model.put("list", list);

        return "userList";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(ModelMap model) {
        User user = new User();

        model.put("userCmd", user);
        model.put("formAction", "loginAction");

        return "login";
    }

    @RequestMapping(value = "/loginAction", method = RequestMethod.POST)
    public String loginAction(@ModelAttribute("userCmd") User user,
                              BindingResult result,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              ModelMap model) {

        request.getSession().setAttribute("user", null);

        user = userDao.getUser(user.getUserName(), user.getPassword());

        if (user != null) {
            request.getSession().setAttribute("user", user);
        } else {
            model.put("message", "login.failed");
            model.put("msgType", "failed");
            return "redirect:login.htm";
        }

        return "redirect:/formBuilder/index.htm";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(ModelMap model, HttpServletRequest request) {
        request.getSession().setAttribute("user", null);

        return "redirect:login.htm";
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPasswordForm(ModelMap model) {
        User user = new User();

        model.put("userCmd", user);
        model.put("formAction", "forgotPasswordAction");

        return "forgotPassword";
    }

    @RequestMapping(value = "/forgotPasswordAction", method = RequestMethod.POST)
    public String forgotPasswordAction(@ModelAttribute("userCmd") User user,
                                       BindingResult result,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       ModelMap model) {

        User currUser = userDao.getUserWithEmail(user.getUserName(), user.getEmail());

        if (currUser != null) {
            //TODO: send email
            model.put("doneMessage", "pass.sent.success");
            model.put("doneMsgType", "success");
            return "redirect:/formBuilder/done.htm";

        }

        model.put("message", "pass.sent.failed");
        model.put("msgType", "failed");

        return "redirect:forgotPassword.htm";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePasswordForm(ModelMap model, HttpServletRequest request) {
        String access = Role.checkRole(Role.ROLE_USER, (User) request.getSession().getAttribute("user"));
        if (access != null) {
            return access;
        }

        User user = new User();

        model.put("userCmd", user);
        model.put("formAction", "changePasswordAction");

        return "changePassword";
    }

    @RequestMapping(value = "/changePasswordAction", method = RequestMethod.POST)
    public String changePasswordAction(@ModelAttribute("userCmd") User user,
                                       BindingResult result,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       ModelMap model) {

        String access = Role.checkRole(Role.ROLE_USER, (User) request.getSession().getAttribute("user"));
        if (access != null) {
            return access;
        }

        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser != null) {
            User currUser = userDao.getUser(sessionUser.getSysId());

            if (currUser.getPassword().equals(user.getOldPassword())) {
                if (user.getPassword().equals(user.getConfirmPassword())) {
                    userDao.changePassword(sessionUser.getUserName(), user.getPassword());
                    model.put("doneMessage", "pass.change.success");
                    model.put("doneMsgType", "success");
                    return "redirect:/formBuilder/done.htm";
                }
            }
        }

        model.put("message", "pass.change.failed");
        model.put("msgType", "failed");

        return "redirect:changePassword.htm";
    }

    @ModelAttribute("yesNoOption")
    public Map getYesNoOption(Locale locale) {
        Map m = new HashMap();

        m.put("1", messageSource.getMessage("yes", null, locale));
        m.put("0", messageSource.getMessage("no", null, locale));

        return m;
    }
    
    @ModelAttribute("customOption")
    public Map getCustomOption(Locale locale) {
        Map m = new HashMap();

        m.put("1", messageSource.getMessage("Secretary", null, locale));
        m.put("10", messageSource.getMessage("Manager", null, locale));

        return m;
    }
    
    @ModelAttribute("ministryOption")
    public List getMinistryOption() {
        List<String> m = new ArrayList<String>();
        
        List<Ministry> ministryList = userDao.getMinistryList();
        
        for(Ministry ministry : ministryList) {
            m.add(ministry.getMinistryName());
        }
        
        return m;
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
    
    

    @RequestMapping(value = "/uniqueUserName", method = RequestMethod.GET)
    public void uniqueUserName(@RequestParam(value = "userName", required = true) String userName,
                               ModelMap model, HttpServletResponse response) throws Exception {

        log.debug("Found username: {}", userName);
        String responseString = "false";

        int count = userDao.getCountWithUserName(userName);
        log.debug("userName: {}, count: {}", userName, count);
        if (count == 0) {
            responseString = "true";
        }

        response.getWriter().print(responseString);
    }
 
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registrationForm(ModelMap model) {
        User user = new User();

        model.put("userCmd", user);
        model.put("formAction", "savePublicUser");

        return "user";
    }
    
    @RequestMapping(value = "/savePublicUser", method = RequestMethod.POST)
    public String savePublicUser(@ModelAttribute("userCmd") User user,
                           BindingResult result, HttpServletRequest request, ModelMap model) {


        if (FormUtil.isEmpty(user.getUserName()) || FormUtil.isEmpty(user.getPassword())) {
            throw new RuntimeException("Required value not found.");
        }
        if (userDao.getCountWithUserName(user.getUserName()) > 0) {
            throw new RuntimeException("User Name must be unique");
        }

        model.put("userCmd", user);
        model.put("formAction", "savePublicUser");

        log.debug("user->save");

        user.setSysId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
        user.setMinistryName("");
        user.setActive(1);
        user.setAdmin(0);
        
        userDao.saveUser(user);

        model.put("message", "msg.form.submitted");
        model.put("msgType", "success");

        return "redirect:userList.htm";
    }
    
    @RequestMapping(value="/newMinistry", method= RequestMethod.GET)
    public String  newMinistry( ModelMap model) {
        
        Ministry ministry = new Ministry();
        
        model.put("ministry", ministry);
        model.put("formAction", "saveMinistry");
        
        return "newMinistry";
    }
    
    @RequestMapping(value="/saveMinistry", method= RequestMethod.POST)
    public String  saveMinistry(@ModelAttribute("ministry")Ministry ministry, ModelMap model) {
        
        userDao.saveMinistry(ministry.getMinistryName());
        
        return "ministryList";
    }
    
    @RequestMapping(value="/ministryList", method= RequestMethod.GET)
    public String  ministryList(ModelMap model) {
        
        model.put("ministryList", userDao.getMinistryList()) ;
        
        return "ministryList";
    }
    
    @RequestMapping(value="/editMinistry")
    public String edidMinistry() {
        return "";
    }
    
    
    
    @RequestMapping(value="/newMilestone", method= RequestMethod.GET)
    public String newMilestone(ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Milestone milestone = new Milestone();
        
        
        //model.put("ministryList", userDao.getMinistryListString());
        model.put("milestoneCmd", milestone);
        model.put("formAction", "saveMilestone");

        
        return "newMilestone";
    }
    
    @RequestMapping(value = "/saveMilestone", method = RequestMethod.POST)
    public String saveMilestone(@ModelAttribute("milestoneCmd") Milestone milestone,
                           BindingResult result, HttpServletRequest request, ModelMap model) {

        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        if (FormUtil.isEmpty( milestone.getMilestoneName() )) {
            throw new RuntimeException("Required value not found.");
        }
        

        model.put("milestoneCmd", milestone);
        //model.put("formAction", "saveUser");

        log.debug("user->save");

        /*user.setSysId(Long.toString(System.nanoTime()) + new Long(new Random().nextLong()));
        User userSession = (User) request.getSession().getAttribute("user");
        user.setMinistryName(userSession.getMinistryName());*/
        
        
        
        /*if(user.getAdmin() == 0) {
            user.setAdmin(10);
        }*/
        
        userDao.saveMilestone(milestone);

        model.put("message", "msg.form.submitted");
        model.put("msgType", "success");

        return "redirect:userList.htm";
    }
    
    @RequestMapping(value="/milestoneList", method= RequestMethod.GET)
    public String milestoneList(ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        
        model.put("milestoneList", userDao.getMilestoneList() );

        
        return "milestoneList";
    }
    
    @RequestMapping(value="/newWorkflow", method= RequestMethod.GET)
    public String newWorkflow(ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        Workflow workflow = new Workflow();
        
        model.put("workflowCmd", workflow);
        model.put("formAction", "saveWorkflow");

        
        return "newWorkflow";
    }
    
    @RequestMapping(value="/saveWorkflow", method= RequestMethod.POST)
    public String nextWorkflow(@ModelAttribute("workflowCmd")Workflow workflow, 
                                    ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        
        userDao.saveWorkflow(workflow);
        
        return "workflowList";
    }
    
    @RequestMapping(value="/workflowList", method= RequestMethod.GET)
    public String workflowList(ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }

        
        model.put("workflowList", userDao.getWorkflowList() );

        
        return "workflowList";
    }
    
    @RequestMapping(value="/designWorkflow", method= RequestMethod.GET)
    public String designWorkflow(@RequestParam(value = "workflowId", required = true) int workflowId,
                                                    ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        
        WorkFlowDetail workflowDetail = new WorkFlowDetail();
        List<WorkFlowDetail> workflowDetailList = userDao.getWorkflowDetailList(workflowId);
        List<WorkFlowDetail> list = new ArrayList<WorkFlowDetail>();
                
        for(WorkFlowDetail s : workflowDetailList) {
            Milestone m1 = userDao.getMilestone(s.getMilestoneId());
            s.setMilestoneName(m1.getMilestoneName());
            
            Milestone m2 = userDao.getMilestone(s.getAcceptMilestoneId());
            s.setAcceptMilestoneName(m2.getMilestoneName());
            
            Milestone m3 = userDao.getMilestone(s.getDeclineMilestoneId());
            s.setDeclineMilestoneName(m3.getMilestoneName());
            
            list.add(s);
        }
        
        model.put("workflowId", workflowId);
        
        workflowDetail.setWorkflowId(workflowId);
        
        model.put("workflowDetail", workflowDetail);
        model.put("workflowDetailList", list);
        model.put("formAction", "saveWorkflowDetail");
        
        return "designWorkflow";
    }
    
    @RequestMapping(value="/saveWorkflowDetail", method= RequestMethod.POST)
    public String saveWorkflowDetail(@ModelAttribute("workflowDetail")WorkFlowDetail workflowDetail,
                                                    ModelMap model, HttpServletRequest request) {
        
        String access = UserAccessChecker.check(request);
        if (access != null) {
            return access;
        }
        
        
        userDao.saveWorkflowDetail(workflowDetail);
        
        return "redirect:designWorkflow.htm?workflowId="+workflowDetail.getWorkflowId();
    }
    
    

}

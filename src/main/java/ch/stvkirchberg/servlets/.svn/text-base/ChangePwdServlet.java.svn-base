/*
 * (c) 2012 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.servlets;

import info.magnolia.cms.exchange.ExchangeException;
import info.magnolia.cms.security.Digester;
import info.magnolia.cms.security.Realm;
import info.magnolia.cms.security.SecuritySupport;
import info.magnolia.cms.security.User;
import info.magnolia.cms.security.UserManager;
import info.magnolia.cms.util.Rule;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;
import info.magnolia.module.admininterface.commands.ActivationCommand;
import info.magnolia.module.exchangesimple.ResourceCollector;
import info.magnolia.module.exchangesimple.SimpleSyndicator;
import info.magnolia.repository.RepositoryConstants;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.models.common.Member;
import ch.stvkirchberg.models.components.ChangePwd;
import ch.stvkirchberg.utils.JcrUtil;
import ch.stvkirchberg.utils.MemberSearchUtil;
import ch.stvkirchberg.utils.NodeUtils;
import ch.stvkirchberg.utils.NotificationUtil;

public class ChangePwdServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(ChangePwdServlet.class);

    @Override
    protected void service(HttpServletRequest req,
                           HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmNewPassword");

        if (StringUtils.isEmpty(password) || !password.equals(confirmPassword)) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihr Passwort stimmt nicht mit der Bestätigung überein oder ist leer. Versuchen Sie es noch einmal");
            return;
        }

        String token = req.getParameter(ChangePwd.ATTR_TOKEN);
        if (StringUtils.isEmpty(token)) {
            handleLoggedInUser(req, resp);
        } else {
            handleResetPassword(req, resp, token);
        }
    }

    private void handleLoggedInUser(HttpServletRequest req,
                                    HttpServletResponse resp) throws IOException {
        WebContext webContext = (WebContext) MgnlContext.getInstance();
        User user = webContext.getUser();
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);

        boolean pwdChange = processChangePwd(req, resp, adminSession, user);
        if (pwdChange) {
            NodeUtils.saveChangesInSession(adminSession);
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Ihr Passwort wurde erfolgreich geändert.");
        } else {
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihr Passwort konnte nicht geändert werden, da ihr Benutzer nicht existiert");
        }
    }

    private void handleResetPassword(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     String token) throws IOException {
        Session adminSession = JcrUtil.getAdminSession(RepositoryConstants.USERS);
        Node pendingResetsRootNode = NodeUtils.getNodeByAbsPath("/" + ResetPwdServlet.PENDING_RESETS, adminSession);
        Node pendingResetNode = NodeUtils.getNode(pendingResetsRootNode, token);
        if (pendingResetNode == null) {
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihr Passwort konnte nicht geändert werden, da ihr Token inkorrekt ist.");
            adminSession.logout();
            return;
        }
        String username = NodeUtils.getStringProperty(pendingResetNode, "username");
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager();
        User user = userManager.getUser(username);

        boolean pwdChange = processChangePwd(req, resp, adminSession, user);

        if (pwdChange) {
            NodeUtils.removeNode(pendingResetNode);
            NodeUtils.saveChangesInSession(adminSession);
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_MSG, "Ihr Passwort wurde erfolgreich geändert.");
        } else {
            adminSession.logout();
            NotificationUtil.redirectToRefererWithMsg(req, resp, NotificationUtil.ATTR_FORM_ERROR_MSG, "Ihr Passwort konnte nicht geändert werden, da ihr Benutzer nicht existiert");
        }
    }

    private boolean processChangePwd(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     Session adminSession,
                                     User user) throws IOException {
        boolean pwdSuccessfulSet = setPasswordToUser(user, req);
        if (!pwdSuccessfulSet) {
            return false;
        }

        Node userNode = MemberSearchUtil.searchUserNode(adminSession, user.getName());
        if (userNode == null) {
            return false;
        }
        Member member = new Member(userNode);
        return replicateUserToAuthor("/admin/" + user.getName(), member.getUuid());
    }

    @SuppressWarnings("static-access")
    private boolean setPasswordToUser(User user,
                                      HttpServletRequest req) {
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager();
        if (user == null || userManager.ANONYMOUS_USER.equals(user.getName())) {
            return false;
        }

        String password = req.getParameter("newPassword");
        String hashedPwd = Digester.getBCrypt(password);
        userManager.setProperty(user, "pswd", JcrUtil.createValue(hashedPwd));
        return true;
    }

    @SuppressWarnings("deprecation")
    private boolean replicateUserToAuthor(String path,
                                          String uuid) {
        UserManager userManager = SecuritySupport.Factory.getInstance().getUserManager(Realm.REALM_SYSTEM.getName());
        User systemUser = userManager.getSystemUser();

        SimpleSyndicator simpleSyndicator = new SimpleSyndicator();
        simpleSyndicator.init(systemUser, RepositoryConstants.USERS, "users", new Rule());
        try {
            simpleSyndicator.setResouceCollector(new ResourceCollector());
        } catch (ExchangeException e1) {
            LOG.error("Unable to set resource collector to syndicator.", e1);
        }

        ActivationCommand activation = new ActivationCommand();
        activation.setRepository(RepositoryConstants.USERS);
        activation.setEnabled(true);
        activation.setItemTypes("mgnl:contentNode");
        activation.setPath(path);
        activation.setRecursive(false);
        activation.setUuid(uuid);
        activation.setSyndicator(simpleSyndicator);
        MgnlContext.setInstance(MgnlContext.getSystemContext());
        return activation.execute(MgnlContext.getSystemContext());
    }
}

/*
 * (c) 2013 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.commands.backup;

import info.magnolia.cms.core.Path;
import info.magnolia.repository.RepositoryConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jcr.Session;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.stvkirchberg.utils.JcrUtil;

public class Backup {

    private static final int MONDAY = 1;

    private static Logger LOG = LoggerFactory.getLogger(Backup.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    File backupFolder;

    File folder;

    {
        // create necessary parent directories
        backupFolder = new File(Path.getAbsoluteFileSystemPath("backup"));
        backupFolder.mkdirs();

        Date today = new Date(System.currentTimeMillis());
        String subFolderName = dateFormat.format(today);
        folder = new File(backupFolder.getAbsoluteFile() + "/" + subFolderName);
        folder.mkdirs();
    }

    public boolean run() {
        LOG.info("backup started");
        try {
            backupRepository(RepositoryConstants.WEBSITE, "/");
            backupRepository("dms", "/");
            backupRepository(RepositoryConstants.USERS, "/admin");
            backupRepository(RepositoryConstants.USERS, "/system");
            backupRepository(RepositoryConstants.USER_GROUPS, "/");
            backupRepository(RepositoryConstants.USER_ROLES, "/");
            backupRepository(RepositoryConstants.CONFIG, "/modules");
            backupRepository(RepositoryConstants.CONFIG, "/server");
            LOG.info("backup finished successful");
            return true;
        } catch (IOException e) {
            LOG.warn("backup finished with failures");
            return false;
        }
    }

    private void backupRepository(String repo,
                                  String path) throws IOException {
        LOG.info("start backup repository [" + repo + "] and path [" + path + "]");
        File xmlFile = createXmlFile(repo, path);
        FileOutputStream fos = null;
        Session adminSession = null;
        try {
            adminSession = JcrUtil.getAdminSession(repo);
            fos = new FileOutputStream(xmlFile);
            info.magnolia.importexport.DataTransporter.executeExport(fos, false, false, adminSession, path, repo, info.magnolia.importexport.DataTransporter.XML);
            LOG.info("backed up repository [" + repo + "] and path [" + path + "]");
        } catch (IOException e) {
            LOG.warn("unable to backup repository [" + repo + "] and path [" + path + "]");
            throw e;
        } finally {
            adminSession.logout();
            IOUtils.closeQuietly(fos);
        }
    }

    private File createXmlFile(String repo,
                               String path) {
        String xmlFileName = repo + path.replaceAll("/", "_") + ".xml";
        return new File(folder.getAbsoluteFile(), xmlFileName);
    }

    protected void deleteOldBackups() {
        deleteBackupsOlderThan3Days();
        deleteBackupsOlderThan4Weeks();
        deleteBackupsOlderThan3Month();
        deleteBackupsOlderThan2Year();
    }

    @SuppressWarnings("deprecation")
    private void deleteBackupsOlderThan3Days() {
        Date today = new Date(System.currentTimeMillis());
        File[] backups = backupFolder.listFiles();
        for (File backup : backups) {
            String backupName = backup.getName();
            try {
                Date backupDate = dateFormat.parse(backupName);
                if (DateUtils.addDays(backupDate, 3).getTime() < today.getTime()) {
                    // Backup is older than three days
                    if (backupDate.getDay() == MONDAY) {
                        continue;
                    }
                    if (isFirstDayOfTheMonth()) {
                        continue;
                    }
                    if (isFirstDayOfTheYear()) {
                        continue;
                    }
                    FileUtils.deleteQuietly(backup);
                    LOG.info("Deleted old backup [" + backupName + "]");
                }
            } catch (ParseException e) {
                LOG.warn("Unable to parse backup date [" + backupName + "]", e);
            }
        }
    }

    private void deleteBackupsOlderThan4Weeks() {
        Date today = new Date(System.currentTimeMillis());
        File[] backups = backupFolder.listFiles();
        for (File backup : backups) {
            String backupName = backup.getName();
            try {
                Date backupDate = dateFormat.parse(backupName);
                if (DateUtils.addWeeks(backupDate, 4).getTime() < today.getTime()) {
                    // Backup is older than 4 months
                    if (isFirstDayOfTheMonth()) {
                        continue;
                    }
                    if (isFirstDayOfTheYear()) {
                        continue;
                    }
                    FileUtils.deleteQuietly(backup);
                    LOG.info("Deleted old backup [" + backupName + "]");
                }
            } catch (ParseException e) {
                LOG.warn("Unable to parse backup date [" + backupName + "]", e);
            }
        }
    }

    private void deleteBackupsOlderThan3Month() {
        Date today = new Date(System.currentTimeMillis());
        File[] backups = backupFolder.listFiles();
        for (File backup : backups) {
            String backupName = backup.getName();
            try {
                Date backupDate = dateFormat.parse(backupName);
                if (DateUtils.addMonths(backupDate, 3).getTime() < today.getTime()) {
                    // Backup is older than 3 Month
                    if (isFirstDayOfTheYear()) {
                        continue;
                    }
                    FileUtils.deleteQuietly(backup);
                    LOG.info("Deleted old backup [" + backupName + "]");
                }
            } catch (ParseException e) {
                LOG.warn("Unable to parse backup date [" + backupName + "]", e);
            }
        }
    }

    private void deleteBackupsOlderThan2Year() {
        Date today = new Date(System.currentTimeMillis());
        File[] backups = backupFolder.listFiles();
        for (File backup : backups) {
            String backupName = backup.getName();
            try {
                Date backupDate = dateFormat.parse(backupName);
                if (DateUtils.addYears(backupDate, 2).getTime() < today.getTime()) {
                    // Backup is older than 2 Years
                    FileUtils.deleteQuietly(backup);
                    LOG.info("Deleted old backup [" + backupName + "]");
                }
            } catch (ParseException e) {
                LOG.warn("Unable to parse backup date [" + backupName + "]", e);
            }
        }
    }

    private boolean isFirstDayOfTheMonth() {
        int dayOfMonth = dateFormat.getCalendar().get(Calendar.DAY_OF_MONTH);
        return (dayOfMonth == 1);
    }

    private boolean isFirstDayOfTheYear() {
        int dayOfYear = dateFormat.getCalendar().get(Calendar.DAY_OF_YEAR);
        return (dayOfYear == 1);
    }
}

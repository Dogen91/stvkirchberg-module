/*
 * (c) 2013 holder contact. All rights reserved. This material is solely and exclusively owned by holder and
 * may not be reproduced elsewhere without prior written approval.
 */

package ch.stvkirchberg.commands.backup;

import info.magnolia.commands.MgnlCommand;
import info.magnolia.context.Context;

public class BackupCommand extends MgnlCommand {

    @Override
    public boolean execute(Context context) throws Exception {
        Backup backup = new Backup();
        if (backup.run()) {
            backup.deleteOldBackups();
        }
        return true;
    }
}

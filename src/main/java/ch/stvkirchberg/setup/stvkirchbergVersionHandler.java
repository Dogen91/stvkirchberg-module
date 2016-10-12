
package ch.stvkirchberg.setup;

import info.magnolia.module.InstallContext;
import info.magnolia.module.admininterface.setup.SimpleContentVersionHandler;
import info.magnolia.module.delta.ModuleBootstrapTask;
import info.magnolia.module.delta.Task;
import info.magnolia.module.model.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is optional and lets you manager the versions of your module, by registering "deltas" to
 * maintain the module's configuration, or other type of content. If you don't need this, simply remove the
 * reference to this class in the module descriptor xml.
 */
public class stvkirchbergVersionHandler extends SimpleContentVersionHandler {

    @Override
    protected List<Task> getBasicInstallTasks(InstallContext installContext) {
        log.warn("Install task: Force reload of bootstrap files");
        return super.getBasicInstallTasks(installContext);
    }

    @Override
    protected List<Task> getDefaultUpdateTasks(Version forVersion) {
        List<Task> defaultUpdateTasks = super.getDefaultUpdateTasks(forVersion);
        log.warn("Update Task: Force reload of bootstrap files");
        defaultUpdateTasks.add(new ModuleBootstrapTask());

        return defaultUpdateTasks;
    }

    @Override
    protected List<Task> getStartupTasks(InstallContext installContext) {
        if ("SNAPSHOT".equalsIgnoreCase(installContext.getCurrentModuleDefinition().getVersion().getClassifier())) {

            log.warn("Starting SNAPSHOT release; forcing reload of module files.");
            return super.getBasicInstallTasks(installContext);
        }
        return new ArrayList<Task>();
    }
}
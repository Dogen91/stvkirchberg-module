package ch.stvkirchberg.models.common;

import info.magnolia.module.templatingkit.templates.pages.STKPage;

public class Page extends STKPage{
	public Page(){
		setTemplateAvailability(new CustomTemplateAvailability());
	}
}

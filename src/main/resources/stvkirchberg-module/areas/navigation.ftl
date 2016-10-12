[#include "/stvkirchberg-module/macros/renderNavigation.ftl"]

[#assign rootPage = cmsfn.root(content, "mgnl:page")!cmsfn.page(content)]

<ul class="nav navbar-nav">
	[@renderNavigation rootPage 1 2 /]
</ul>

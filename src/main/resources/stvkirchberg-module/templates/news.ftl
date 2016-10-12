[#assign rootPage = cmsfn.root(content, "mgnl:page")!cmsfn.page(content)]
<html style="background-image: url(${cmsfn.link('dms',rootPage.bgImageDmsUUID!)})">
	<head>
		[#include "/stvkirchberg-module/templates/includes/head.ftl"]
	</head>
	<body>
		<div id="container">
			<div id="header">
				<div id="gymnast">
				
				</div>
				<div id="headLine">
					[#assign rootPage = cmsfn.root(content, "mgnl:page")!]
					<h1>
						<img src="${cmsfn.link('dms',rootPage.headerImageDmsUUID!)}" height="77px" /><a href="${cmsfn.link(rootPage)}">${rootPage.title!}</a>
					</h1>
				</div>
				<div id="mainNav">
					[#include "/stvkirchberg-module/areas/navigation.ftl" ]
				</div>
			</div>
			<div id="content">
				<div id="secondNav">
					[#include "/stvkirchberg-module/areas/secondLevelNavigation.ftl" ]
				</div>
				<div id="mainContent">
					<h1>${content.title!}</h1>
					[@cms.area name="main" /]
					<div class="spacer" />
				</div>
			</div>
			<div id="footer" class="clear">
				[@cms.area name="sponsors" /]
				<div class="clear"></div>
			</div>
		</div>
	</body>
</html>
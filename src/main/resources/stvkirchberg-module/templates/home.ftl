[#assign rootPage = cmsfn.root(content, "mgnl:page")!cmsfn.page(content)]
<html style="background-image: url(${cmsfn.link('dms',rootPage.bgImageDmsUUID!)})">
	<head>
		[#include "/stvkirchberg-module/templates/includes/head.ftl"]
	</head>
	<body class="home">
		<div class="container">
			[#include "/stvkirchberg-module/templates/includes/header.ftl"]
			
			<div class="row">
				<div class="col-sm-8">
					[@cms.area name="homeMain" /]
				</div>
				<div class="col-sm-4">
					<div class="teaser">
						[@cms.area name="teaser" /]
					</div>
					<div class="sponsors">
						[@cms.area name="sponsors" /]
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
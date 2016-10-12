[#assign rootPage = cmsfn.root(content, "mgnl:page")!cmsfn.page(content)]
<html style="background-image: url(${cmsfn.link('dms',rootPage.bgImageDmsUUID!)})">
	<head>
		[#include "/stvkirchberg-module/templates/includes/head.ftl"]
	</head>
	<body class="home">
		<div class="container">
			<nav class="navbar navbar-default">
				<div class="container-fluid">
			    	<div class="navbar-header">
			    		<div class="row">
							[#assign currentPage = cmsfn.page(content)!]
							<div class="col-xs-3">
								<img src="${cmsfn.link('dms',rootPage.headerImageDmsUUID!)}" class="img-responsive" />
							</div>
							<div class="col-xs-9">
								<a href="${cmsfn.link(currentPage)}">${content.title!}</a>
								<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
							        <span class="sr-only">Toggle navigation</span>
							        <span class="icon-bar"></span>
							        <span class="icon-bar"></span>
							        <span class="icon-bar"></span>
					     		</button>
							</div>
						</div>
			    	</div>
					
					<div class="row">
				    	<div class="collapse navbar-collapse col-sm-12" id="bs-example-navbar-collapse-1">
				      		[#include "/stvkirchberg-module/areas/navigation.ftl" ]
				    	</div>
			    	</div>
			  	</div>
			</nav>			
			
			<div class="row">
				<div class="col-sm-8">
					[@cms.area name="homeMain" /]
				</div>
				<div class="col-sm-4">
					[@cms.area name="teaser" /]
					[@cms.area name="sponsors" /]
				</div>
			</div>
		</div>
	</body>
</html>
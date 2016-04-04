[#assign rootPage = cmsfn.root(content, "mgnl:page")!cmsfn.page(content)]
<html style="background-image: url(${cmsfn.link('dms',rootPage.bgImageDmsUUID!)})">
	<head>
		[@cms.init /]
		<title>STV Kirchberg - ${content.title!}</title>
		<link rel="stylesheet" type="text/css" href="${contextPath}/.resources/stvkirchberg-module/css/base.css" />
		<!--[if IE]>
			<link rel="stylesheet" type="text/css" href="${contextPath}/.resources/stvkirchberg-module/css/ie.css" />
		<![endif]-->
		<link rel="stylesheet" type="text/css" href="${contextPath}/.resources/stvkirchberg-module/css/jquery.fancybox-1.3.4.css" />
		
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.fancybox-1.3.4.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/slides.min.jquery.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/base.js"></script>
	</head>
	<body class="home">
		<div id="container">
			<div id="header">
				<div id="gymnast">
				
				</div>
				<div id="headLine">
					[#assign currentPage = cmsfn.page(content)!]
					<h1>
						<img src="${cmsfn.link('dms',rootPage.headerImageDmsUUID!)}" height="77px" /><a href="${cmsfn.link(currentPage)}">${content.title!}</a>
					</h1>
				</div>
				<div id="mainNav">
					[#include "/stvkirchberg-module/areas/navigation.ftl" ]
				</div>
			</div>
			<div id="contentWrapper">
				<div id="content">
					[@cms.area name="homeMain" /]
				</div>
				<div id="teaser">
					[@cms.area name="teaser" /]
				</div>
				[@cms.area name="sponsors" /]
				<div class="clear"></div>
				<div id="footer">
					
				</div>
			</div>
		</div>
	</body>
</html>
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
		<link rel="stylesheet" type="text/css" href="${contextPath}/.resources/stvkirchberg-module/css/fullcalendar.css" />

		
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.mousewheel-3.0.4.pack.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.fancybox-1.3.4.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/slides.min.jquery.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/jwplayer/jwplayer.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/base.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/fullcalendar.min.js"></script>
		<script type="text/javascript" src="${contextPath}/.resources/stvkirchberg-module/js/gcal.js"></script>
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
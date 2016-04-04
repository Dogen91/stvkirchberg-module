[#macro renderNavigation pageNode startDepth maxDepth ]
	[#assign currentPage = cmsfn.page(content)]

	[#list cmsfn.children(pageNode, "mgnl:page") as childPage]
		[#if currentPage.@path?starts_with(childPage.@path)]
			[#if childPage.@depth > startDepth]
		        [#if !(childPage.hideInNav?has_content && childPage.hideInNav)]
					<li class="selected">
		            	<a href="${cmsfn.link(childPage)}"><span>${childPage.title!childPage.@name}</span></a>
			            [#if childPage.@depth <  maxDepth]
				        	<ul>
				        		[@renderNavigation childPage startDepth maxDepth /]
				        	</ul>
				        [/#if]
		        	</li>
		    	[/#if]
		    [#else]
		        [#if childPage.@depth <  maxDepth]
		        	<ul>
		        		[@renderNavigation childPage startDepth maxDepth /]
		        	</ul>
		        [/#if]
	        [/#if]
	        
	        
		[#else]
			[#if childPage.@depth > startDepth]
				[#if !(childPage.hideInNav?has_content && childPage.hideInNav)]
					<li>
			            <a href="${cmsfn.link(childPage)}"><span>${childPage.title!childPage.@name}</span></a>
			        </li>
		        [/#if]
	        [/#if]
		[/#if]
	[/#list]
[/#macro]
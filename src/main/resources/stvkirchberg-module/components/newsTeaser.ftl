[#list model.newsEntries as newsEntry ]
	<div class="newsTeaser">
   		<h1>${newsEntry.title}</h1>
		<p>
   			[#if newsEntry.image?has_content]
   				<img src="${newsEntry.image.link}" alt="" />
   			[/#if]
   			${newsEntry.leadText}
   		</p>
   		<p><a href="${model.webContext.contextPath}${newsEntry.link}"><span class="glyphicon glyphicon-hand-right" aria-hidden="true"></span> Zur Seite ${newsEntry.title}</a></p>
   	</div>
[/#list]

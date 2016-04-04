[#list model.newsEntries as newsEntry ]
	<div class="newsTeaser">
   		<h1><a href="${model.webContext.contextPath}${newsEntry.link}">${newsEntry.title}</a></h1>
   		<a href="${model.webContext.contextPath}${newsEntry.link}">
   			[#if newsEntry.image?has_content]
   				<img class="media photo pos-2" src="${newsEntry.image.link}" alt="" />
   			[/#if]
   			${newsEntry.leadText}
   		</a>
   		<p><a class="readMore" href="${model.webContext.contextPath}${newsEntry.link}">Zur Seite ${newsEntry.title}</a></p>
   	</div>
[/#list]

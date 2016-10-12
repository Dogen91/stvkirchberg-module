<div class="list-group">
	<div class="list-group-item list-group-item-info">
		Sponsoren
	</div>
	[#list components as component ]
		<div class="list-group-item">
	   		[@cms.component content=component /]
	   	</div>
	[/#list]
</div>
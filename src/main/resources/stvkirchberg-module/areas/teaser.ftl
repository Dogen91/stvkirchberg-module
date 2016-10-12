<div class="list-group">
	<div class="list-group-item list-group-item-warning">
		Hinweise
	</div>
	[#list components as component ]
		<div class="list-group-item">		
			[@cms.component content=component /]
		</div>
	[/#list]
</div>


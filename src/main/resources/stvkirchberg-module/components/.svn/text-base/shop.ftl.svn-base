<div class="shop">
	<h2>${content.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<form action="${model.webContext.contextPath}/.stvkirchberg/shopOrder" method="post">
		[@cms.area name="shopItems" /]
		
		<fieldset>
			<input type="hidden" name="receiver" value="${model.receiverMail}" />
			<div class="button-wrapper">
	    		<input type="submit" value="Bestellung abschicken" />
			</div>
		</fieldset>
	</form>
</div>
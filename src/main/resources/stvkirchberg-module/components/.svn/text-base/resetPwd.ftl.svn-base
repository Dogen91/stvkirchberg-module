<div class="resetPwd">
	<h2>${model.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<div class="form-wrapper">
		<form enctype="multipart/form-data" action="${model.webContext.contextPath}/.stvkirchberg/resetPwd" method="post" id="resetPwdForm">
            <fieldset>
				<div>
			        <label for="username">
			            <span>Benutzername</span>
			        </label>
	        		<input type="text" value="" id="username" name="username" />
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Passwort anfordern">
				</div>
			</fieldset>
	    </form>
	</div>
</div>
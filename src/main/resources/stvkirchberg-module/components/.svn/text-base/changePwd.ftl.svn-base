<div class="changePwd">
	<h2>${model.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<div class="form-wrapper">
		<form enctype="multipart/form-data" action="${model.webContext.contextPath}/.stvkirchberg/changePwd" method="post" id="changePwdForm">
            <fieldset>
	            [#if model.token?has_content]
	            	<input type="hidden" name="token" value="${model.token}" />
	            [#else]
					<div>
				        <label for="currentPassword">
				            <span>Aktuelles Passwort</span>
				        </label>
		        		<input type="password" value="" id="currentPassword" name="currentPassword" />
					</div>
	            [/#if]
				<div>
			        <label for="newPassword">
			            <span>Neues Passwort</span>
			        </label>
	        		<input type="password" value="" id="newPassword" name="newPassword" />
				</div>
				<div>
			        <label for="confirmNewPassword">
			            <span>Neues Passwort best&auml;tigen</span>
			        </label>
	        		<input type="password" value="" id="confirmNewPassword" name="confirmNewPassword" />
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Passwort abschicken">
				</div>
			</fieldset>
	    </form>
	</div>
</div>
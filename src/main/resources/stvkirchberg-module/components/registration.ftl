<div class="registration">
	<h2>${model.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<div class="form-wrapper">
    	<form enctype="multipart/form-data" action="${model.webContext.contextPath}/.stvkirchberg/registration" method="post" id="registration">
            <fieldset>
            	<div>
			        <label for="username">
			            <span>Benutzername</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="username" name="username" required="required">
				</div>
				<div>
			        <label for="prename">
			            <span>Vorname</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="prename" name="prename" required="required">
				</div>
				<div>
			        <label for="lastname">
			            <span>Nachname</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="lastname" name="lastname" required="required">
				</div>
				<div>
			        <label for="mail">
			            <span>E-Mail</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="mail" name="mail" required="required">
				</div>
				<div>
			        <label for="password">
			            <span>Passwort</span>
			        </label>
	        		<input type="password" maxlength="50" value="" id="password" name="password" required="required">
				</div>
				<div>
			        <label for="passwordConfirmation">
			            <span>Passwort best&auml;tigen</span>
			        </label>
	        		<input type="password" maxlength="50" value="" id="passwordConfirmation" name="passwordConfirmation" required="required">
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Submit">
				</div>
			</fieldset>
	    </form>
    </div>
</div>
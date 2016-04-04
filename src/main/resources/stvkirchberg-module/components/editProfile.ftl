<div class="editProfile">
	<h2>${model.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<div class="form-wrapper">
		<form enctype="multipart/form-data" action="${model.webContext.contextPath}/.stvkirchberg/updateProfile" method="post" id="editProfile">
            <fieldset>
            	<input type="hidden" name="uuid" value="${model.member.uuid!}" />
            	<input type="hidden" name="username" value="${model.member.username!}" />
            	<input type="hidden" name="prename" value="${model.member.prename!}" />
            	<input type="hidden" name="lastname" value="${model.member.lastname!}" />
				<div>
			        <label for="mail">
			            <span>E-Mail</span>
			        </label>
	        		<input type="text" value="${model.member.mail!}" id="mail" name="mail" />
				</div>
				<div>
			        <label for="phoneNumber">
			            <span>Telefonnummer</span>
			        </label>
	        		<input type="text" value="${model.member.phoneNumber!}" id="phoneNumber" name="phoneNumber" />
				</div>
				<div>
			        <label for="mobilePhoneNumber">
			            <span>Mobiltelefonnummer</span>
			        </label>
	        		<input type="text" value="${model.member.mobilePhoneNumber!}" id="mobilePhoneNumber" name="mobilePhoneNumber" />
				</div>
				<div>
			        <label for="address">
			            <span>Adresse</span>
			        </label>
	        		<input type="text" value="${model.member.address!}" id="address" name="address" />
				</div>
				<div>
			        <label for="postalCode">
			            <span>Postleitzahl</span>
			        </label>
	        		<input type="text" value="${model.member.postalCode!}" id="postalCode" name="postalCode" />
				</div>
				<div>
			        <label for="city">
			            <span>Ort</span>
			        </label>
	        		<input type="text" value="${model.member.city!}" id="city" name="city" />
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Abschicken">
				</div>
			</fieldset>
	    </form>
	</div>
</div>
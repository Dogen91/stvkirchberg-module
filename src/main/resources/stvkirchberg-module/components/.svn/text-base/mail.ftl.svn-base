<div class="mail">
	<h2>${model.title!}</h2>
	[#if model.notification.formErrorMsg?has_content]
		<p class="error">${model.notification.formErrorMsg}</p>
	[/#if]
	[#if model.notification.formMsg?has_content]
		<p class="info">${model.notification.formMsg}</p>
	[/#if]
	<div class="form-wrapper">
    	<form enctype="multipart/form-data" action="${model.webContext.contextPath}/.stvkirchberg/sendMail" method="post" id="mailForm">
            <fieldset>
				<div>
			        <label for="receivers">
			            <span>Empf&auml;nger</span>
			        </label>
	        		<input type="text" value="" id="receivers" name="receivers">
				</div>
				<div>
			        <label for="subject">
			            <span>Betreff</span>
			        </label>
	        		<input type="text" value="" id="subject" name="subject">
				</div>
				<div>
			        <label for="attachment">
			            <span>Datei hinzuf&uuml;gen</span>
			        </label>
	        		<input type="file" value="" id="attachment" name="attachment">
				</div>
				<div>
			        <label for="message">
			            <span>Nachricht</span>
			        </label>
	        		<textarea rows="8" cols="20" name="message" id="message"></textarea>
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Submit">
				</div>
			</fieldset>
	    </form>
    </div>
	
	<h2>Empf&auml;nger Suche</h2>
	<div class="form-wrapper">
    	<form enctype="multipart/form-data" action="" method="post" id="memberSearch">
            <fieldset>
				<div>
			        <label for="name">
			            <span>Name</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="name" name="name">
				</div>
				<div>
			        <label for="squad">
			            <span>Riege</span>
			        </label>
				    <fieldset>
				    	<select name="squad" id="squad">
				        	<option value="">-</option>
							[#list model.squadMap?keys as key]
								<option value="${model.squadMap[key]}">${key}</option>
							[/#list]
				       	</select>
				    </fieldset>
				</div>
				<div>
			        <label for="role">
			            <span>Rolle</span>
			        </label>
				    <fieldset>
				    	<select name="role" id="role">
				        	<option value="">-</option>
							[#list model.roleMap?keys as key]
								<option value="${model.roleMap[key]}">${key}</option>
							[/#list]
				       	</select>
				    </fieldset>
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Submit">
				</div>
			</fieldset>
	    </form>
    </div>
    [#if model.members?has_content]
	    <div class="selectReceiver">
	    	<form enctype="multipart/form-data" action="" method="post" id="selectReceiverForm">
		    	<table cellspacing="1" cellpadding="1" border="0">     
		    		<thead>         
		    			<tr>
		    				<th scope="col"><input class="selectAll checkbox" type="checkbox" name="selectAll" value="" /></th>
		    				<th scope="col">Anrede</th>
		    				<th scope="col">Vorname</th>
		    				<th scope="col">Nachname</th>
		    				<th scope="col">E-Mail</th>
		    			</tr>
		    	    </thead>
		    	    <tbody>
		    	    	[#list model.members as member ]
			    	    	<tr>
			    	    		<td><input type="checkbox" name="receivers" value="${member.mail}" class="receiverCheckbox checkbox" /></td>
			    	    		<td>${member.salutation}</td>
			    	    		<td>${member.prename}</td>
			    	    		<td>${member.lastname}</td>
			    	    		<td>${member.mail}</td>
							</tr>
						[/#list]
					</tbody>
				</table>
				<div class="button-wrapper selectReceiverButton">
	    			<input type="submit" value="Add selected" />
				</div>
			</form>
	    </div>
	[/#if]
</div>
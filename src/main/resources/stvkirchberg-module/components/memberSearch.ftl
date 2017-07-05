<div class="memberSearch">
	<h2>${model.title!}</h2>
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
				<div>
			        <label for="stvNumber">
			            <span>STV Nummer</span>
			        </label>
	        		<input type="text" maxlength="50" value="" id="stvNumber" name="stvNumber">
				</div>
				<div class="button-wrapper">
	    			<input type="submit" value="Submit">
				</div>
			</fieldset>
	    </form>
    </div>
    [#if model.members?has_content]
	    <div class="memberResult">
	    	<table cellspacing="1" cellpadding="1" border="0">     
	    		<thead>         
	    			<tr>
	    				<th scope="col">Anrede</th>
	    				<th scope="col">Vorname</th>
	    				<th scope="col">Nachname</th>
	    				<th scope="col">E-Mail</th>
	    				<th scope="col">Telefon</th>
	    				<th scope="col">Mobile</th>
	    				<th scope="col">Adresse</th>
	    				<th scope="col">PLZ</th>
	    				<th scope="col">Ort</th>
	    				<th scope="col">Geburtsdatum</th>
	    				<th scope="col">Eintrittsdatum</th>
	    				<th scope="col">Aktiv</th>
	    				<th scope="col">Ehrenmitglied Seit</th>
	    				<th scope="col">Ehrenmitglied</th>
	    				<th scope="col">G&ouml;nner</th>
	    				<th scope="col">Passivmitglied</th>
	    				<th scope="col">Vereinsmitglied</th>
	    				<th scope="col">Bilderverbot</th>
	    				<th scope="col">Bemerkung</th>
	    				<th scope="col">STV Nummer</th>
	    				[#list model.roleMap?keys as key]
							<th>${key}</th>
						[/#list]
	    				[#list model.squadMap?keys as key]
							<th>${key}</th>
						[/#list]
	    			</tr>
	    	    </thead>
	    	    <tbody>
	    	    	[#list model.members as member ]
		    	    	<tr>
		    	    		<td>${member.salutation}</td>
		    	    		<td>${member.prename}</td>
		    	    		<td>${member.lastname}</td>
		    	    		<td>${member.mail}</td>
		    	    		<td>${member.phoneNumber}</td>
		    	    		<td>${member.mobilePhoneNumber}</td>
		    	    		<td>${member.address}</td>
		    	    		<td>${member.postalCode}</td>
		    	    		<td>${member.city}</td>
		    	    		<td>${member.birthdate}</td>
		    	    		<td>${member.entryDate}</td>
		    	    		<td>
		    	    			[#if member.active]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>${member.honoraryMemberSince}</td>
		    	    		<td>
		    	    			[#if member.honoraryMember]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>
		    	    			[#if member.benefactor]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>
		    	    			[#if member.passivMember]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>
		    	    			[#if member.clubMember]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>
		    	    			[#if member.imgForbidden]
		    	    				Ja
		    	    			[#else]
		    	    				Nein
		    	    			[/#if]
		    	    		</td>
		    	    		<td>${member.remark}</td>
		    	    		<td>${member.stvNumber}</td>
		    	    		[#list model.roleMap?keys as key]
								<td>
									[#list member.roles as role]
										[#if role = model.roleMap[key]]X[/#if] 
									[/#list]
								</td>
							[/#list]
		    				[#list model.squadMap?keys as key]
								<td>
									[#list member.squads as squad]
										[#if squad = model.squadMap[key]]X[/#if] 
									[/#list]
								</td>
							[/#list]
						</tr>
					[/#list]
				</tbody>
			</table>
	    </div>
	    <form class="memberSearchFilter" action="${model.webContext.contextPath}/.stvkirchberg/exportToXsl" method="post">
	    	<input type="hidden" value="${model.memberSearchFilter.name!}" name="name" />
	    	<input type="hidden" value="${model.memberSearchFilter.squad!}" name="squad" />
	    	<input type="hidden" value="${model.memberSearchFilter.role!}" name="role" />
	    	<div class="button-wrapper exportButton">
    			<input type="submit" value="Export to Excel">
			</div>
	    </form>
	[/#if]
</div>
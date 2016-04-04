[#list model.mainLeader as mainLeader ]
	<tr>
		<th>[#if mainLeader_index == 0]${model.squad!}[/#if]</th>
		<td><b>${mainLeader.prename!} ${mainLeader.lastname!}</b></td>
		<td>${mainLeader.address!}</td>
		<td>${mainLeader.city!}</td>
		<td><a href="mailto:${mainLeader.mail!}">${mainLeader.mail!}</a></td>
	</tr>
[/#list]
[#list model.leaders as leader ]
	<tr>
		<th></th>
		<td>${leader.prename!} ${leader.lastname!}</td>
		<td>${leader.address!}</td>
		<td>${leader.city!}</td>
		<td><a href="mailto:${leader.mail!}">${leader.mail!}</a></td>
	</tr>
[/#list]
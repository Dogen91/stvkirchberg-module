<div class="shopItem">
	<dl class="media photo">
  		<dt><img src="${model.image.link}" alt="" /></dt>
  		<dd class="zoom"><a rel="showbox" title="Zoom on this image" href="${model.bigImage.link}">Zoom</a></dd>
  		<dd class="caption">${model.image.caption!}</dd>
  
  		<dd class="longdesc"><p>${model.image.description!}</p></dd>
	</dl>
	<h3>${content.title}</h3>
	<fieldset>
		<input type="hidden" value="${content.title}" name="article_${model.id}"/>
		<div>
			<label for="cut_${model.id}">
				<span>Schnitt:</span>
			</label>
	    	<select name="cut_${model.id}" id="cut_${model.id}" class="cut">
	            <option value="man">Herren</option>
	            <option value="lady">Damen</option>
            </select>
		</div>
		<div class="sizesMan">
			<label for="sizesMan_${model.id}">
				<span>Gr&ouml;sse</span>
			</label>
	    	<select name="sizesMan_${model.id}" id="sizesMan_${model.id}">
	    		[#list model.sizesMan as size]
	            	<option value="${size}">${size}</option>
	            [/#list]
            </select>
		</div>
		<div class="sizesLady" style="display:none;">
			<label for="sizesLady_${model.id}">
				<span>Gr&ouml;sse</span>
			</label>
	    	<select name="sizesLady_${model.id}" id="sizesLady_${model.id}">
	    		[#if model.sizesLady?size == 0]
	    			[#list model.sizesMan as size]
		            	<option value="${size}">${size}</option>
		            [/#list]
	    		[#else]
		            [#list model.sizesLady as size]
		            	<option value="${size}">${size}</option>
		            [/#list]
		        [/#if]
            </select>
		</div>
		<div>
			<label for="amount_${model.id}">
				<span>Anzahl:</span>
			</label>
	    	<input type="text" name="amount_${model.id}" id="amount_${model.id}" value="0" />
		</div>
	</fieldset>
	<div class="clear"></div>
</div>
$(document).ready(function(){
	
	/***
	 * Table
	***/
	$('table:not(.memberResult table) tr:odd td').css("background-color", "#EEEEEE");
		
	/***
	 FancyBox
	***/
	$('.media .zoom a').fancybox();
	
	/***
	 DataTable
	***/
	$(".memberResult table").dataTable({
		"aaSorting": [[2,'asc'], [1,'asc']],
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bInfo": false,
		"fnDrawCallback": function(){
			$('.memberResult table tr td').css("background-color", "#FFFFFF");
			$('.memberResult table tr:odd td').css("background-color", "#EEEEEE");
		}
	});
	
	$(".selectReceiver table").dataTable({
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bInfo": false,
		"bSortable": false,
		"fnInitComplete": function(){
			$('.selectReceiver table tr:odd td').css("background-color", "#EEEEEE");
		}
	});
	
	
	/***
	  Export to XSL
	 ***/
	$(".exportToXsl").click(function(e){
		
	})
	
	/***
	 * Mail Component
	 */
	bindSelectAllCheckbox()
	
	bindAddSelectedReceiverButton();
	
	$(".mail #memberSearch").on("submit", function(e){
		e.preventDefault();
		$.ajax({
			beforeSend: showAjaxLoader(),
			url: document.location.href,
			data: $(this).serialize(),
			success: function(data) {
				$(".selectReceiver").remove();
				var newReceiverTable = $(".selectReceiver",$(data));
				newReceiverTable.insertAfter($("#memberSearch").parent());
				bindSelectAllCheckbox();
				bindAddSelectedReceiverButton();
				hideAjaxLoader();
			}
		});
	});
	
	/***
	 * Shop Component
	 */
	$(".shopItem select.cut").on("change", function(){
		var value = $(this).val();
		var parent = $(this).closest('.shopItem');
		if(value == 'lady'){
			$('.sizesLady', parent).show();
			$('.sizesMan', parent).hide();
		}else {
			$('.sizesLady', parent).hide();
			$('.sizesMan', parent).show();
		}
	});
});

function showAjaxLoader(){
	$('body').append('<div class="ajaxLayer"><span></span></div>');
}

function hideAjaxLoader(){
	$('.ajaxLayer').remove();
}

function bindSelectAllCheckbox(){
	$(".selectAll").on('click', function(){
		if($(this).is(":checked")){
			$(".receiverCheckbox").each(function(){
				$(this).prop('checked', true);
			});
		}else{
			$(".receiverCheckbox").each(function(){
				$(this).prop('checked', false);
			});
		}
	});
}

function bindAddSelectedReceiverButton(){
	$(".selectReceiverButton input").on('click', function(e){
		e.preventDefault();
		var receivers = $("#receivers").val();
		$(".receiverCheckbox:checked").each(function(){
			receivers = receivers + $(this).val() + ";";
		});
		$("#receivers").val(receivers);
	});
}
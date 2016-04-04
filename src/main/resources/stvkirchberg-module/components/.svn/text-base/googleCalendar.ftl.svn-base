<div class="googleCalendar">
	<div id="googleCalendarId">
	
	</div>
</div>
<script type='text/javascript'>
	$(document).ready(function() {
	    $('#googleCalendarId').fullCalendar({
	        events: '${model.calendarFeed}',
		    eventClick: function(event) {
		        return false;
		    },
		    firstDay: 1,
		    weekMode: 'variable',
		    timeFormat: 'H(:mm)',
		    loading: function(isLoading, view){
		    	if(isLoading){
		    		showAjaxLoader();
		    	}else {
		    		hideAjaxLoader();
		    	}
		    },
	        buttonText: {
		        today: 'Heute'
		    },
		    monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'],
		    dayNamesShort: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa']
	    });
	});
</script>
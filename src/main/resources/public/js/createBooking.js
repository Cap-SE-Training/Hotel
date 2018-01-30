//$(document).ready(function() {
//    $( "#startDatepicker" ).datepicker();
//    $( "#endDatepicker" ).datepicker();
//} );


$(document).ready(function() {
    var dateFormat = "mm/dd/yy",
        from = $( "#startDatepicker" ).datepicker({
          defaultDate: "+1w",
          changeMonth: true,
        })
        .on( "change", function() {
          to.datepicker( "option", "minDate", getDate( this ) );
        }),
        to = $( "#endDatepicker" ).datepicker({
        defaultDate: "+1w",
        changeMonth: true,
        })
        .on( "change", function() {
        from.datepicker( "option", "maxDate", getDate( this ) );
        });

    function getDate( element ) {
      var date;
      try {
        date = $.datepicker.parseDate( dateFormat, element.value );
      } catch( error ) {
        date = null;
      }

      return date;
    }
} );
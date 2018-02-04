var tableHelperGuests;
var tableElementGuests;
var tableSelectedGuestId;
var guestsSelector;
var guests = [];

var booking = {
    guests: [],
    rooms: []
};

$('document').ready(function() {
    var dateFormat = "mm/dd/yy";
    var from = $( "#startDatepicker" ).datepicker({
        defaultDate: "+1w",
        changeMonth: true
    });
    var to = $( "#endDatepicker" ).datepicker({
        defaultDate: "+1w",
        changeMonth: true
    });
    from.on( "change", function() {
        to.datepicker( "option", "minDate", getDate( this ) );
    });
    to.on( "change", function() {
        from.datepicker( "option", "maxDate", getDate( this ) );
    });

    guestsSelector = $('#guestsSelector');

    // Load DataTable with data format.
    tableElementGuests = $('#guestsTable');
    tableHelperGuests = new DataTableHelper(tableElementGuests, {
        bLengthChange: false,
        bInfo: false,
        searching: false,
        paging: false,
        rowId: 'id',
        columns: [
            { "data": function( data, type, row ){
                    return data.firstName + " " + data.lastName;
                } },
            { "data": "email" }
        ]
    });
    // Retrieve all guests
    ajaxJsonCall('GET', '/api/guests/', null, function(result) {
        guests = result;
    });
    $('#addGuest').on('click', function() {
        $('#guestModal').modal('show');
    });
    $('#removeGuest').on('click', function() {
        var guestId = tableHelperGuests.getSelectedRowId();
        tableHelperGuests.dataTable.row('#'+guestId).remove().draw();
        booking.guests = _.filter(booking.guests, function(guest) {
            return guest.id !== parseInt(guestId);
        });
    });
    $('#findGuests').on('click', function() {
        $('#findGuestsModal').modal('show');
        guestsSelector.empty();
        _.map(guests, function(guest) {
            if (_.contains(_.pluck(booking.guests, 'id'), guest.id)) {
                return;
            }
            var option = $('<option value="' + guest.id + '">' + guest.firstName + ' ' + guest.lastName + ' (' + guest.email + ')</option>');
            guestsSelector.append(option);
        });
        guestsSelector.trigger("chosen:updated");
        setTimeout(function() {
            guestsSelector.chosen();
        }, 200);
    });
    $('#guestForm').submit(function(event) {
        event.preventDefault();
        var guest = getFormData();
        ajaxJsonCall('POST', '/api/guests/create', guest, function(result) {
            $('#guestModal').modal('hide');
            toastr.success('Added "' + guest.firstName + ' ' + guest.lastName + '" to Guests!');
            $('#guestForm').get(0).reset();
            tableHelperGuests.dataTable.row.add(result).draw();
            booking.guests.push(result);
            guests.push(result);
        }, handleError);
    });

    $('#findGuestsForm').submit(function(event) {
        event.preventDefault();
        $('#findGuestsModal').modal('hide');

        var foundGuestIds = _.map(guestsSelector.val(), function(id) { return parseInt(id) });
        var filteredGuestIds = _.filter(foundGuestIds, function(guestId) {
            return !_.contains(_.pluck(booking.guests, 'id'), guestId);
        });
        var foundGuests = _.map(filteredGuestIds, function(id) {
            return _.findWhere(guests, {
                id: id
            });
        });
        _.each(foundGuests, function(guest) {
            tableHelperGuests.dataTable.row.add(guest).draw();
            booking.guests.push(guest);
        });
        $('#findGuestsForm').get(0).reset();
        guestsSelector.trigger("chosen:updated");
    });
});

function createGuestHtml(guest) {
    var card = $('<div class="card col-6"></div>');
    var cardBody = $('<div class="card-body"></div>');
    var cardTitle = $('<h5 class="card-title"></div>');
    cardTitle.append(guest.firstName + ' ' + guest.lastName);
    var cardSubtitle = $('<h6 class="card-subtitle mb-2 text-muted"></h6>');
    cardSubtitle.append(guest.email);
    cardBody.append(cardTitle);
    cardBody.append(cardSubtitle);
    card.append(cardBody);
    return card;
}

function getFormData() {
    return {
        firstName : $("#firstName").val(),
        lastName : $("#lastName").val(),
        email : $("#email").val(),
        telephoneNumber : $("#telephoneNumber").val(),
        address: {
            street : $("#street").val(),
            houseNumber : $("#houseNumber").val(),
            postalCode : $("#postalCode").val(),
            city : $("#city").val(),
            country : $("#country").val()
        }
    };
}

function getDate( element ) {
    var date;
    try {
        date = $.datepicker.parseDate( dateFormat, element.value );
    } catch( error ) {
        date = null;
    }

    return date;
}
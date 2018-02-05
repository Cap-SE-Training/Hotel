var booking = {
    guests: [],
    rooms: []
};

$('document').ready(function () {
    var progress = new Progress({
        container: $('#progressBar'),
        previousButton: $('previousButton'),
        nextButton: $('#nextButton'),
        finish: function() {
            ajaxJsonCall('POST', '/api/bookings/create', booking, function (result) {
                toastr.success('Successfully added a booking!');
                window.location.href = '/bookings.html';
                }, handleError);
        }
    });
    progress.setSteps([{
        name: 'Date(s)',
        container: $('#dates'),
        init: function() {
            var from = $("#fromDatepicker");
            var to = $("#toDatepicker");

            from.on("change", function (event) {
                var value = $(this).val();
                if (!value) {
                    return;
                }
                var date = new Date(value);
                if (!to.val() || new Date(to.val()).getTime() >= date.getTime()) {
                    to.attr("min", value);
                }
                booking.startDate = date.getTime();
                progress.check();
            });

            to.on("change", function () {
                var value = $(this).val();
                if (!value) {
                    from.attr("max", false);
                    return;
                }
                var date = new Date(value);
                if (!from.val() || new Date(from.val()).getTime() <= date.getTime()) {
                    from.attr("max", value);
                }
                booking.endDate = date.getTime();
                progress.check();
            });
        },
        check: function() {
            return booking.startDate && booking.endDate && (booking.startDate <= booking.endDate);
        }
    }, {
        name: 'Guest(s)',
        container: $('#guests'),
        init: function() {
            var guests = [];
            // Retrieve all guests
            ajaxJsonCall('GET', '/api/guests/', null, function (result) {
                guests = result;
            });

            var guestsSelector = $('#guestsSelector');

            // Load DataTable with data format.
            var tableElementGuests = $('#guestsTable');
            var tableHelperGuests = new DataTableHelper(tableElementGuests, {
                bLengthChange: false,
                bInfo: false,
                searching: false,
                paging: false,
                rowId: 'id',
                columns: [
                    {
                        "data": function (data, type, row) {
                            return data.firstName + " " + data.lastName;
                        }
                    },
                    {"data": "email"}
                ]
            });
            $('#addGuest').on('click', function () {
                $('#guestModal').modal('show');
            });
            $('#removeGuest').on('click', function () {
                var guestId = tableHelperGuests.getSelectedRowId();
                tableHelperGuests.dataTable.row('#' + guestId).remove().draw();
                booking.guests = _.filter(booking.guests, function (guest) {
                    return guest.id !== parseInt(guestId);
                });
            });
            $('#findGuests').on('click', function () {
                $('#findGuestsModal').modal('show');
                guestsSelector.empty();
                _.map(guests, function (guest) {
                    if (_.contains(_.pluck(booking.guests, 'id'), guest.id)) {
                        return;
                    }
                    var option = $('<option value="' + guest.id + '">' + guest.firstName + ' ' + guest.lastName + ' (' + guest.email + ')</option>');
                    guestsSelector.append(option);
                });
                guestsSelector.trigger("chosen:updated");
                setTimeout(function () {
                    guestsSelector.chosen();
                }, 200);
            });
            $('#guestForm').submit(function (event) {
                event.preventDefault();
                var guest = {
                    firstName: $("#firstName").val(),
                    lastName: $("#lastName").val(),
                    email: $("#email").val(),
                    telephoneNumber: $("#telephoneNumber").val(),
                    address: {
                        street: $("#street").val(),
                        houseNumber: $("#houseNumber").val(),
                        postalCode: $("#postalCode").val(),
                        city: $("#city").val(),
                        country: $("#country").val()
                    }
                };
                ajaxJsonCall('POST', '/api/guests/create', guest, function (result) {
                    $('#guestModal').modal('hide');
                    toastr.success('Added "' + guest.firstName + ' ' + guest.lastName + '" to Guests!');
                    $('#guestForm').get(0).reset();
                    tableHelperGuests.dataTable.row.add(result).draw();
                    booking.guests.push(result);
                    guests.push(result);
                    progress.check();
                }, handleError);
            });
            $('#findGuestsForm').submit(function (event) {
                event.preventDefault();
                $('#findGuestsModal').modal('hide');

                var foundGuestIds = _.map(guestsSelector.val(), function (id) {
                    return parseInt(id)
                });
                var filteredGuestIds = _.filter(foundGuestIds, function (guestId) {
                    return !_.contains(_.pluck(booking.guests, 'id'), guestId);
                });
                var foundGuests = _.map(filteredGuestIds, function (id) {
                    return _.findWhere(guests, {
                        id: id
                    });
                });
                _.each(foundGuests, function (guest) {
                    tableHelperGuests.dataTable.row.add(guest).draw();
                    booking.guests.push(guest);
                });
                $('#findGuestsForm').get(0).reset();
                guestsSelector.trigger("chosen:updated");
                progress.check();
            });
        },
        check: function() {
            return booking.guests.length > 0;
        }
    }, {
        name: 'Room(s)',
        container: $('#rooms'),
        init: function () {
            // Load DataTable with data format.
            var tableElementRooms = $('#roomsTable');
            var tableHelperRooms = new DataTableHelper(tableElementRooms, {
                bLengthChange: false,
                bInfo: false,
                searching: false,
                paging: false,
                rowId: 'id',
                columnDefs: [ {
                    orderable: false,
                    className: 'select-checkbox',
                    targets:   0
                }],
                select: {
                    style:    'os',
                    selector: 'td:first-child'
                },
                columns: [
                    {
                        "data": null,
                        render: function( data, type, row ) {
                            if ( type === 'display' ) {
                                return '<input type="checkbox" class="select-checkbox">';
                            }
                            return data;
                        },
                        className: "dt-body-center"
                    },
                    { "data": "name" },
                    { "data": "number" },
                    { "data": "roomType.type" },
                    { "data": "size" },
                    { "data": "price" }
                ]
            }, true);
            ajaxJsonCall('GET', '/api/rooms/', null, function(rooms) {
                tableHelperRooms.dataTable.clear();
                tableHelperRooms.dataTable.rows.add(rooms);
                tableHelperRooms.dataTable.columns.adjust().draw();
                $('input[type="checkbox"].select-checkbox').on('change', function(event) {
                    var rowId = $(event.currentTarget).closest('tr').attr('id');
                    var room = tableHelperRooms.dataTable.row('#' + rowId).data();
                    if ($(this).prop('checked')) {
                        booking.rooms.push(room);
                    } else {
                        booking.rooms = _.filter(booking.rooms, function (bookingRoom) {
                            return bookingRoom.id !== room.id;
                        });
                    }
                    progress.check();
                });
            }, null);
        },
        check: function() {
            return booking.rooms.length > 0;
        }
    }]);
});

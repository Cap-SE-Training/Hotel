var tableHelper;
var tableElement;
var selectedId;

$(document).ready(function () {

    getBookings();

    tableElement = $('#bookingsTable');
        tableHelper =  new DataTableHelper(tableElement, {
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "rooms.0.id" },
            { "data": "guests.0.firstName" },
            { "data": "guests.0.lastName" },
            //https://legacy.datatables.net/ref#mData
            {
                "data": "startDate",
                render: formatDateColumn
            },
            {
                "data": "endDate",
                render: formatDateColumn
            },
            {
                "data": null,
                "mData": function ( source, type, val ) {
                    return moment(source.endDate).diff(source.startDate, 'days') + ' days';
                }
            },
            { "data": "paymentMethod" }
        ]
    });

$('#remove').on('click', function(event) {
    var booking = tableHelper.getSelectedRowData();
    bootboxConfirm("Are you sure you want to delete this Booking?", function(result){
        removeBooking(booking, function() {
            toastr.success('Removed Booking' );
            updateTable();
        }, handleError);
    });
});
})

function formatDateColumn(d) {
    if (!d) {
        return;
    }

    return moment(d).format("DD/MM/YYYY");
}

function getBookings() {
    console.log("Getting All Bookings...");

    $.ajax({
        url:"/api/bookings/",
        type:"get",
        success: function(bookings) {
            console.log("This is the data: ", bookings);
            table.clear();
            table.rows.add(bookings);
            table.columns.adjust().draw();
        }
    });
}

function createBooking(booking, successCallback, errorCallback) {
    console.log("Creating Booking..")
    ajaxJsonCall('POST', '/api/bookings/create', booking, successCallback, errorCallback);
}

function editBooking(booking, successCallback, errorCallback) {
    console.log("Editing Booking..")
    ajaxJsonCall('POST', '/api/bookings/edit', booking, successCallback, errorCallback);
}

function removeBooking(booking, successCallback, errorCallback) {
    console.log("Removing booking..")
    ajaxJsonCall('DELETE', '/api/bookings/delete/' + booking.id, null, successCallback, errorCallback);
}

function updateTable() {
    console.log("Updating table..");

    $('button.controls').prop('disabled', selectedId === undefined);
    ajaxJsonCall('GET', '/api/bookings/', null, function(guests) {
      table.clear();
      table.rows.add(bookings);
      table.columns.adjust().draw();}, null)
}
function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};

var tableHelper;
var tableElement;
var selectedId;

$(document).ready(function () {

    getBookings();

    tableElement = $('#table');
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
        bootboxConfirm("Are you sure you want to delete this booking?", function(result){
            if (result == true){
                removeBooking(booking, function() {
                    console.log(booking.guests);
                    toastr.success('Removed booking from "' + booking.guests[0].firstName + " " +
                        booking.guests[0].lastName + '" from Bookings!');
                    getBookings();
                },
                handleError);
            }
            else{
                $('#modal').modal('toggle');
            }
        });
    });
});

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
            $('button.controls').prop('disabled', selectedId === undefined);
            tableHelper.dataTable.clear();
            tableHelper.dataTable.rows.add(bookings);
            tableHelper.dataTable.columns.adjust().draw();
        }
    });
}

function postBooking() {
    console.log("Getting All Bookings...");

    $.ajax({
        url:"/api/bookings/",
        type:"post",
        success: function(bookings) {
            console.log("This is the data: " + bookings);
            tableHelper.dataTable.clear();
            tableHelper.dataTable.rows.add(bookings);
            tableHelper.dataTable.columns.adjust().draw();
        }
    });
}

function removeBooking(booking, successCallback, errorCallback) {
    console.log("Removing booking..")
    ajaxJsonCall('DELETE', '/api/bookings/delete/' + booking.id, null, successCallback, errorCallback);
}
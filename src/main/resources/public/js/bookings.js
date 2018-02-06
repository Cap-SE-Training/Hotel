$(document).ready(function () {

    getBookings();

    table =  $('#table').DataTable({
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
            table.clear();
            table.rows.add(bookings);
            table.columns.adjust().draw();
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
            table.clear();
            table.rows.add(bookings);
            table.columns.adjust().draw();
        }
    });
}
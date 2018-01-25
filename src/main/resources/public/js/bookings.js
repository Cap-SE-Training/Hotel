$(document).ready(function () {

    getBookings();

    table =  $('#table').DataTable({
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "roomId" },
            { "data": "guest.name" },
            { "data": "guest.lastName" },
            { "data": "startDate" },
            { "data": "endDate" },
            { "data": "checkedIn" },
            { "data": "checkedOut" },
            { "data": "paid" },
            { "data": "paymentMethod" }
        ]
    });
})
function getBookings() {
    console.log("Getting All Bookings...");

    $.ajax({
        url:"http://localhost:8080/api/booking/",
        type:"get",
        success: function(bookings) {
            console.log("This is the data: " + bookings);
            table.clear();
            table.rows.add(bookings);
            table.columns.adjust().draw();
        }
    });
}

function postBooking() {
    console.log("Getting All Bookings...");

    $.ajax({
        url:"http://localhost:8080/api/booking/",
        type:"post",
        success: function(bookings) {
            console.log("This is the data: " + bookings);
            table.clear();
            table.rows.add(bookings);
            table.columns.adjust().draw();
        }
    });
}
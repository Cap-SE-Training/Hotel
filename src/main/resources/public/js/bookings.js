$(document).ready(function () {

    getBookings();
    googleTranslateElementInit();

    table =  $('#table').DataTable({
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "rooms.0.id" },
            { "data": "guests.0.firstName" },
            { "data": "guests.0.lastName" },
            //https://legacy.datatables.net/ref#mData
            { "mData": function date(data, type, dataToSet) {
                    return formatDate(data.startDate);
                }
            },
            { "mData": function date(data, type, dataToSet) {
                    return formatDate(data.endDate);
                }
            },
            { "mData": function date(data, type, dataToSet) {
                    return formatDate(data.checkedIn);
                }
            },
            { "mData": function date(data, type, dataToSet) {
                    return formatDate(data.checkedOut);
                }
            },
            { "mData": function date(data, type, dataToSet) {
                    return formatDate(data.paid);
                }
            },
            { "data": "paymentMethod" }
        ]
    });
})

function formatDate(date){
    if(date){
        return date.dayOfMonth + "-" + date.monthValue + "-" + date.year;
    } else {
        return null;
    }
}

function getBookings() {
    console.log("Getting All Bookings...");

    $.ajax({
        url:"/api/bookings/",
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
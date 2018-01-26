$(document).ready(function () {

    getBookings();

    table =  $('#table').DataTable({
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "rooms.0.id" },
            { "data": "guests.0.firstName" },
            { "data": "guests.0.lastName" },
            { "mData": function date(data, type, dataToSet) {
            		    return data.startDate.dayOfMonth + "-" + data.startDate.monthValue + "-" + data.startDate.year;
            	        }
            },
            { "mData": function date(data, type, dataToSet) {
                        		    return data.endDate.dayOfMonth + "-" + data.endDate.monthValue + "-" + data.endDate.year;
                        	        }
            },
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
        url:"/api/booking/",
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
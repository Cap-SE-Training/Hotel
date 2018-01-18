$(document).ready(function () {
console.log("document ready")

    // Load DataTable with data format.
    $('#table').DataTable({
        columns: [
           { "data": function( data, type, row ){
                return data.firstName + " " + data.lastName;
           } },
//           { "data": "address" },
           { "data": "email" },
           { "data": "telephoneNumber" },
        ]
    });

    $("#newGuestForm").on('submit', function(e) {
        console.log("Submitted new guest form");
        postData();
        $('#newGuestModal').modal('hide');
        $("#firstName").val("");
        $("#lastName").val("");
        $("#email").val("");
        $("#telephoneNumber").val("");

    });

    getData();
})

function getData() {
    console.log("Getting data...");

    $.ajax({
        url:"http://localhost:8080/api/guests/",
        type:"get",
        success: function(guests) {
            console.log("This is the data: " + guests);
            $('#table').DataTable().clear();
            $('#table').DataTable().rows.add(guests);
            $('#table').DataTable().columns.adjust().draw();
        }
    });
}


function postData(){
    console.log("Posting data...");
    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();
    var email = $("#email").val();
    var telephoneNumber = $("#telephoneNumber").val();

    var newGuest = {
        firstName : firstName,
        lastName : lastName,
        email : email,
        telephoneNumber : telephoneNumber
    };

    var validJsonGuest = JSON.stringify(newGuest);
    console.log(validJsonGuest);

    $.ajax({
        url:"http://localhost:8080/api/guests/create",
        type:"post",
        contentType: "application/json",
        data: validJsonGuest,
        success: function(result) {
            console.log("Succes");
            getData();
        }
    });
}
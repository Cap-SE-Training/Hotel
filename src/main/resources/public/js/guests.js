var table;
var selectedId;
$(document).ready(function () {
console.log("document ready")

    // Load DataTable with data format.
   table =  $('#table').DataTable({
        rowId: 'id',
        bLengthChange: false,
        columns: [
           { "data": function( data, type, row ){
                return data.firstName + " " + data.lastName;
           } },
//           { "data": "address" },
           { "data": "email" },
           { "data": "telephoneNumber" },
        ]
    });

    $('#table').on('click', 'tr', function() {
        $(table).find('tr.selected').removeClass('selected');
        var id = table.row(this).id();
        if (id !== selectedId) {
            $(this).addClass('selected');
            selectedId = table.row( this ).id();
        } else {
            selectedId = undefined;
        }

        $('button.controls').prop('disabled', selectedId === undefined);
    });

    // Reset Form after submit
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

    $('#remove').on('click', function(event) {
        bootbox.confirm({
            message: "Are you sure you want to delete this guest?",
            buttons: {
                confirm: {
                    label: 'Yes',
                    className: 'btn-danger'
                },
                cancel: {
                    label: 'No',
                    className: 'btn-primary'
                }
            },
            callback: function(result){
                console.log(result);
                if (result === false){
                    console.log('ACTION CANCELED!!!');
                }else {
                var guest = table.row('#' + selectedId).data();
                console.log(guest);
                removeGuest(guest, function() {
                    toastr.success('Removed "' + guest.firstName + ' ' + guest.lastName + '" from Guests!');
                    getData();
                }, handleError);
                }
            }
        });
     });
})

function getData() {
    console.log("Getting data...");

    $.ajax({
        url:"http://localhost:8080/api/guests/",
        type:"get",
        success: function(guests) {
            console.log("This is the data: " + guests);
            table.clear();
            table.rows.add(guests);
            table.columns.adjust().draw();
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

function handleError(error) {
    toastr.success('Something bad happened');
    console.log(error);
};

function removeGuest(guest, successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/guests/delete/' + guest.id,
        type: 'DELETE',
        success: successCallback,
        error: errorCallback
    });
}
var tableHelper;
var tableElement;
var selectedId;
var edit = false;

$(document).ready(function () {
    console.log("Guest document ready")

    // Load DataTable with data format.
    tableElement = $('#guestsTable');
    tableHelper =  new DataTableHelper(tableElement, {
        bLengthChange: false,
        rowId: 'id',
        columns: [
           { "data": function( data, type, row ){
                return data.firstName + " " + data.lastName;
           } },
           //{ "data": "address" },
           { "data": "email" },
           { "data": "telephoneNumber" },
        ]
    });

    updateTable();

    $('#create').on('click', function(event) {
        $('#guestModal .modal-title').html('Creating a guest');
        $('#guestModal').modal('show');
    });
    $('#edit').on('click', function(event) {
        edit = true;
        var guest = tableHelper.getSelectedRowData();
        console.log(guest)
        setFormData(guest);
        $('#guestModal .modal-title').html('Editing ' + guest.name);
        $('#guestModal').modal('show');
        
    });
    $('#remove').on('click', function(event) {
        var guest = tableHelper.getSelectedRowData();
        bootboxConfirm("Are you sure you want to delete this guest?", function(result){
            removeGuest(guest, function() {
                toastr.success('Removed "' + guest.firstName + ' ' + guest.lastName + '" from Guests!');
                updateTable();
            }, handleError);
        });
    });
    $('#guestForm').submit(function(event) {
        event.preventDefault();
        $('#guestModal').modal('hide');
        if (edit) {
            handleEditFormSubmit();
        } else {
            handleCreateFormSubmit();
        }
    });
})

function handleCreateFormSubmit() {
    var data = getFormData();
    createGuest(data, function(result) {
        toastr.success('Added "' + data.firstName + ' ' + data.lastName + '" to Guests!');
        $('#guestForm').get(0).reset();
        updateTable();
    }, handleError);
}

function handleEditFormSubmit() {
    var guest = tableHelper.getSelectedRowData();
    var data = getFormData();
    _.extend(guest, data);
    editGuest(data, function(result) {
        toastr.success('Edited "' + data.firstName + ' ' + data.lastName + '"');
        $('#guestForm').get(0).reset();
        updateTable();
        edit = false;
    }, handleError);
}
function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};

function createGuest(guest, successCallback, errorCallback) {
    console.log("Creating guest..")
    ajaxJsonCall('POST', '/api/guests/create', guest, successCallback, errorCallback);
}

function editGuest(guest, successCallback, errorCallback) {
    console.log("Editing guest..")
    ajaxJsonCall('POST', '/api/guests/edit', guest, successCallback, errorCallback);
}

function removeGuest(guest, successCallback, errorCallback) {
    console.log("Removing guest..")
    ajaxJsonCall('DELETE', '/api/guests/delete/' + guest.id, null, successCallback, errorCallback);
}

function getFormData() {
    return {
        firstName : $("#firstName").val(),
        lastName : $("#lastName").val(),
        email : $("#email").val(),
        telephoneNumber : $("#telephoneNumber").val()
    };
}

function setFormData(guest) {
    $('#firstName').val(guest.firstName);
    $('#lastName').val(guest.lastName);
    $('#email').val(guest.email);
    $('#telephoneNumber').val(guest.telephoneNumber);
}

//
//    // Reset Form after submit
//    $("#newGuestForm").on('submit', function(e) {
//        console.log("Submitted new guest form");
//        $('#newGuestModal').modal('hide');
//        $("#firstName").val("");
//        $("#lastName").val("");
//        $("#email").val("");
//        $("#telephoneNumber").val("");
//    });

function updateTable() {
    console.log("Updating table..");

    $('button.controls').prop('disabled', selectedId === undefined);
    $.get('api/guests/', function(guests) {
        tableHelper.dataTable.clear();
        tableHelper.dataTable.rows.add(guests);
        tableHelper.dataTable.columns.adjust().draw();
    })
}


//function removeGuest(guest, successCallback, errorCallback) {
//    $.ajax({
//        contentType: 'application/json',
//        url: '/api/guests/delete/' + guest.id,
//        type: 'DELETE',
//        success: successCallback,
//        error: errorCallback
//    });
//}
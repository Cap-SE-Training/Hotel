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
           { "data": "email" },
           { "data": "telephoneNumber" },
           { "data": function( data, type, row ){
               return data.address.street + " " + data.address.houseNumber;
           } },
           { "data": "address.postalCode" },
           { "data": "address.city" },
           { "data": "address.country" }
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
            if (result == true){
            removeGuest(guest, function() {
                toastr.success('Removed "' + guest.firstName + ' ' + guest.lastName + '" from Guests!');
                updateTable();
            }, handleError);}
            else{
            $('#modal').modal('toggle');;}


        });

    });
    $('#guestForm').submit(function(event) {
        event.preventDefault();
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
        $('#guestModal').modal('hide');
    }, handleError);
}

function handleEditFormSubmit() {
    var guest = tableHelper.getSelectedRowData();
    var data = getFormData();
    _.extend(guest, data);
    editGuest(guest, function(result) {
        console.log("editing");
        console.log(guest);
        toastr.success('Edited "' + data.firstName + ' ' + data.lastName + '"');
        $('#guestForm').get(0).reset();
        updateTable();
        $('#guestModal').modal('hide');
        edit = false;
    }, handleError);
}
function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};

function createGuest(data, successCallback, errorCallback) {
    console.log("Creating guest..")

    var address = {
        street: data.street,
        houseNumber: data.houseNumber,
        postalCode: data.postalCode,
        city: data.city,
        country: data.country
    };

    var guest = {
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        telephoneNumber: data.telephoneNumber
    };

    ajaxJsonCall('POST', '/api/address/create', address,
        function(result) {
            guest.address = result;
            ajaxJsonCall('POST', '/api/guests/create', guest, successCallback, errorCallback);
    }, errorCallback);
}

function editGuest(data, successCallback, errorCallback) {

    console.log("Editing guest..")
    var editedAddress = {
        street: data.street,
        houseNumber: data.houseNumber,
        postalCode: data.postalCode,
        city: data.city,
        country: data.country
    };

    var editedGuest = {
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        telephoneNumber: data.telephoneNumber
    };

    var address = tableHelper.getSelectedRowData().address;
    _.extend(address, editedAddress);
    var guest = tableHelper.getSelectedRowData();

    ajaxJsonCall('POST', '/api/address/edit', address,
        function(result) {
            editedGuest.address = result;
            _.extend(guest, editedGuest);

            ajaxJsonCall('POST', '/api/guests/edit', guest, successCallback, errorCallback);
    }, errorCallback);
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
        telephoneNumber : $("#telephoneNumber").val(),
        street : $("#street").val(),
        houseNumber : $("#houseNumber").val(),
        postalCode : $("#postalCode").val(),
        city : $("#city").val(),
        country : $("#country").val()
    };
}

function setFormData(guest) {
    $('#firstName').val(guest.firstName);
    $('#lastName').val(guest.lastName);
    $('#email').val(guest.email);
    $('#telephoneNumber').val(guest.telephoneNumber);
    $("#street").val(guest.address.street);
    $("#houseNumber").val(guest.address.houseNumber);
    $("#postalCode").val(guest.address.postalCode);
    $("#city").val(guest.address.city);
    $("#country").val(guest.address.country);
}

function updateTable() {
    console.log("Updating table..");

    $('button.controls').prop('disabled', selectedId === undefined);
    ajaxJsonCall('GET', '/api/guests/', null, function(guests) {
      tableHelper.dataTable.clear();
      tableHelper.dataTable.rows.add(guests);
      tableHelper.dataTable.columns.adjust().draw();}, null)
}
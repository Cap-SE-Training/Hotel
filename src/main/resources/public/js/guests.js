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

    $('#guestsTable').on('click', 'tr', function() {
        $(table).find('tr.selected').removeClass('selected');
        var id = table.row(this).id();
        if (id !== selectedId) {
            $(this).addClass('selected');
            selectedId = table.row(this).id();
        } else {
            selectedId = undefined;
        }
    });

    $('#create').on('click', function(event) {
        $('#newGuestModal .modal-title').html('Creating a guest');
        $('#newGuestModal').modal('show');
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
        $("#street").val("");
        $("#houseNumber").val("");
        $("#postalCode").val("");
        $("#city").val("");
        $("#country").val("");

    $('#edit').on('click', function(event) {
        edit = true;
        var guest = tableHelper.getSelectedRowData();
        console.log(guest)
        setFormData(guest);
        $('#newGuestModal .modal-title').html('Editing ' + guest.name);
        $('#newGuestModal').modal('show');
    });
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
            callback: function(result) {
                console.log(result);
                if (result === false) {
                    console.log('ACTION CANCELED!!!');
                } else {
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
    });
    $('#newGuestForm').submit(function(event) {
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
        $('#newGuestModal').modal('hide');
        $('#newGuestForm').get(0).rHEADeset();
        updateTable();
    }, handleError);
}

function postData() {
    var addressId = postAddress()
}

function postAddress() {
    console.log("Posting address data...");

    var firstName = $("#firstName").val();
    var lastName = $("#lastName").val();
    var email = $("#email").val();
    var telephoneNumber = $("#telephoneNumber").val();

    var street = $("#street").val();
    var houseNumber = $("#houseNumber").val();
    var postalCode = $("#postalCode").val();
    var city = $("#city").val();
    var country = $("#country").val();

    var newAddress = {
        street: street,
        houseNumber: houseNumber,
        postalCode: postalCode,
        city: city,
        country: country
    };

    var validJsonAddress = JSON.stringify(newAddress);
    console.log(validJsonAddress);

    $.ajax({
            url: "/api/address/create",
            type: "post",
            contentType: "application/json",
            data: validJsonAddress,
            success: function(result) {
                console.log("address creation successful");

                var newGuest = {
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    telephoneNumber: telephoneNumber,
                    address: {
                        id: result.id
                    }
                };

                createGuest(newGuest, function(result) {
                    toastr.success('Added "' + newGuest.firstName + " " + newGuest.lastName + '" to Guests!');
                    $('#newGuestForm').get(0).reset();
                    updateTable();
                }, handleError);
            }});
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

function handleEditFormSubmit() {
    var guest = tableHelper.getSelectedRowData();
    var data = getFormData();
    _.extend(guest, data);
    editGuest(guest, function(result) {
        toastr.success('Edited "' + data.firstName + ' ' + data.lastName + '"');
        $('#newGuestForm').get(0).reset();
        updateTable();
        edit = false;
    }, handleError);
}
function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};

function createGuest(guest, successCallback, errorCallback) {
    console.log("Creating guest..");
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

function updateTable() {
    console.log("Updating table..");

    $('button.controls').prop('disabled', selectedId === undefined);
    ajaxJsonCall('GET', '/api/guests/', null, function(guests) {
      tableHelper.dataTable.clear();
      tableHelper.dataTable.rows.add(guests);
      tableHelper.dataTable.columns.adjust().draw();}, null)
}

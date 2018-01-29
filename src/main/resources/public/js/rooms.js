var tableHelper;
var tableElement;
var selectedId;
var edit = false;

$(document).ready(function() {
   getRoomTypes(function(result) {
        result.forEach(function(roomType) {
            $('#type').append('<option value=' + roomType.id + '>' + roomType.type + '</option>');
            $('#filterType').append('<div onclick="tableHelper.dataTable.draw()"><label> <input type="checkbox" value="' + roomType.id
                + '" id="' + roomType.type + '" checked>' + roomType.type + '</label></div>');
        });
    });
    getRoomSizes(function(result) {
        result.forEach(function(size) {
            $('#filterSize').append('<div onclick="tableHelper.dataTable.draw()"><label> <input type="checkbox" value="' + size
                            + '" id="size-' + size + '" checked>' + size + '</label></div>');
        });
    });

    tableElement = $('#roomsTable');
    tableHelper = new DataTableHelper(tableElement, {
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "roomStatus" },
            { "data": "name" },
            { "data": "number" },
            { "data": "roomType.type" },
            { "data": "size" },
            { "data": "price" }
        ]
    });

    updateTable();

    $('#create').on('click', function(event) {
        $('#roomModal .modal-title').html('Creating a room');
        $('#roomModal').modal('show');
    });
    $('#edit').on('click', function(event) {
        edit = true;
        var room = tableHelper.getSelectedRowData();
        setFormData(room);
        $('#roomModal .modal-title').html('Editing ' + room.name);
        $('#roomModal').modal('show');
    });
    $('#remove').on('click', function(event) {
        var room = tableHelper.getSelectedRowData();
        bootboxConfirm('Are you sure you want to delete this room?', function(result) {
            removeRoom(room, function() {
                toastr.success('Removed "' + room.name + '" from Rooms!');
                updateTable();
            }, handleError);
        });
    });
    $('#roomForm').submit(function(event) {
        event.preventDefault();
        $('#roomModal').modal('hide');
        if (edit) {
            handleEditFormSubmit();
        } else {
            handleCreateFormSubmit();
        }
    });
});

function handleCreateFormSubmit() {
    var data = getFormData();
    createRoom(data, function(result) {
        toastr.success('Added "' + data.name + '" to Rooms!');
        $('#roomForm').get(0).reset();
        updateTable();
    }, handleError);
}

function handleEditFormSubmit() {
    var room = tableHelper.getSelectedRowData();
    var data = getFormData();
    _.extend(room, data);
    editRoom(room, function(result) {
        toastr.success('Edited "' + room.name + '"');
        $('#roomForm').get(0).reset();
        updateTable();
        edit = false;
    }, handleError);
}

function getFormData() {
    return {
        roomStatus: $('#status').val(),
        name: $('#name').val(),
        number: $('#number').val(),
        roomType: {
            id: $('#type').val()
        },
        size: $('#size').val(),
        price: $('#price').val()
    };
}

function setFormData(room) {
    console.log(room);
    $('#status option:eq("' + room.roomStatus + '")').prop('selected', true)
    $('#name').val(room.name);
    $('#number').val(room.number);
    $('#type').val(room.roomType.id);
    $('#size').val(room.size);
    $('#price').val(room.price);
}

function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};

function getRoomTypes(successCallback, errorCallback) {
    ajaxJsonCall('GET', '/api/room_types/', null, successCallback, errorCallback);
}

function getRoomSizes(successCallback, errorCallback) {
    ajaxJsonCall('GET', '/api/rooms/sizes', null, successCallback, errorCallback);
}

function createRoom(room, successCallback, errorCallback) {
    ajaxJsonCall('POST', '/api/rooms/create', room, successCallback, errorCallback);
}

function editRoom(room, successCallback, errorCallback) {
    ajaxJsonCall('POST', '/api/rooms/edit', room, successCallback, errorCallback);
    console.log(room.id);
}

function removeRoom(room, successCallback, errorCallback) {
    ajaxJsonCall('DELETE', '/api/rooms/delete/' + room.id, null, successCallback, errorCallback);
}

function updateTable() {
    $('button.controls').prop('disabled', selectedId === undefined);
    ajaxJsonCall('GET', '/api/rooms/', null, function(rooms) {
          tableHelper.dataTable.clear();
          tableHelper.dataTable.rows.add(guests);
          tableHelper.dataTable.columns.adjust().draw();}, null)
}


$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var status = data[0].toLowerCase();
        var type = data[3];
        var price = data[5];
        var size = data[4];

        return ($('#' + status).is(":checked") && $('#' + type).is(":checked") && $('#size-' + size).is(":checked"));
    }
);
var table;
var tableElement;
var selectedId;

$(document).ready(function() {
    getRoomTypes(function(result) {
        result.forEach(function(roomType) {
            $('#type').append('<option value=' + roomType.id + '>' + roomType.type + '</option>');
            $('#filterType').append('<div onclick="table.draw()"><label> <input type="checkbox" value="' + roomType.id
                + '" id="' + roomType.type + '" checked>' + roomType.type + '</label></div>');
        });
        console.log(result);
    });
    getRoomSizes(function(result) {
        result.forEach(function(size) {
            $('#filterSize').append('<div onclick="table.draw()"><label> <input type="checkbox" value="' + size
                            + '" id="size-' + size + '" checked>' + size + '</label></div>');
        });
    });
    tableElement = $('#roomsTable');
    table = tableElement.DataTable({
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
    tableElement.on('click', 'tr', function() {
        $(tableElement).find('tr.selected').removeClass('selected');
        var id = table.row(this).id();
        if (id !== selectedId) {
            $(this).addClass('selected');
            selectedId = table.row( this ).id();
        } else {
            selectedId = undefined;
        }

        $('button.controls').prop('disabled', selectedId === undefined);
    });

    updateTable();


    $('#remove').on('click', function(event) {
        bootbox.confirm({
            message: "Are you sure you want to delete this room?",
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
                var room = table.row('#' + selectedId).data();
                console.log(room);
                removeRoom(room, function() {
                    toastr.success('Removed "' + room.name + '" from Rooms!');
                    updateTable();
                }, handleError);
            }
        });
    });
    $('#addRoom').submit(function(event) {
        event.preventDefault();

        $('#addRoomModal').modal('hide');

        var data = {
            roomStatus: $('#status').val(),
            name: $('#name').val(),
            number: $('#number').val(),
            roomType: {
                id: $('#type').val()
            },
            size: $('#size').val(),
            price: $('#price').val()
        };

        console.log(data);
        createRoom(data, function(result) {
           toastr.success('Added "' + data.name + '" to Rooms!');
           console.log(result);
           $('#addRoom').get(0).reset();
           updateTable();
        }, handleError);
    });
});

function handleError(error) {
    toastr.success('Something bad happened');
    console.log(error);
};

function getRoomTypes(successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/room_types/',
        type: 'GET',
        dataType: 'json',
        success: successCallback,
        error: errorCallback
    });
}

function getRoomSizes(successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/rooms/sizes',
        type: 'GET',
        dataType: 'json',
        success: successCallback,
        error: errorCallback
    });
}

function createRoom(room, successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/rooms/create',
        data: JSON.stringify(room),
        type: 'POST',
        dataType: 'json',
        success: successCallback,
        error: errorCallback
    });
}

function removeRoom(room, successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/rooms/delete/' + room.id,
        type: 'DELETE',
        success: successCallback,
        error: errorCallback
    });
}

function updateTable() {
    $('button.controls').prop('disabled', selectedId === undefined);
    $.get('/api/rooms/', function(rooms) {
        table.clear();
        table.rows.add(rooms);
        table.columns.adjust().draw();
    });
}

$.fn.dataTable.ext.search.push(
    function( settings, data, dataIndex ) {
        var status = data[0].toLowerCase();
        var type = data[3];
        var price = data[5];
        var size = data[4];

        if ($('#' + status).is(":checked") && $('#' + type).is(":checked") && $('#size-' + size).is(":checked")) {
            return true;
        }
        return false;
    }
);
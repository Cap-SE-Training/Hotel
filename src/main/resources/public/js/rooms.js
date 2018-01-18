var table;
var tableElement;
var selectedId;

$(document).ready(function() {
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
        $(tableElement).find('tr.selected').each(function() {
            $(this).removeClass('selected');
        });
        var id = table.row( this ).id();
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
                id: $('#type').val(),
                type: $('#type option:selected').text()
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

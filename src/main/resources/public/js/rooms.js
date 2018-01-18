var table;
var tableElement;
var selectedId;

$(document).ready(function() {
    tableElement = $('#personsTable');
    table = tableElement.DataTable({
        bLengthChange: false,
        rowId: 'id',
        columns: [
            { "data": "name" },
            { "data": "age" }
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
            message: "Are you sure you want to delete this person?",
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
                var person = table.row('#' + selectedId).data();
                console.log(person);
                removePerson(person, function() {
                    toastr.success('Removed "' + person.name + '" from Persons!');
                    updateTable();
                }, handleError);
            }
        });
    });
    $('#addPerson').submit(function(event) {
        event.preventDefault();
        $('#addPersonModal').modal('hide');

        var data = {
            name: $('#name').val(),
            age: $('#age').val()
        };

        createPerson(data, function(result) {
           toastr.success('Added "' + data.name + '" to Persons!');
           $('#addPerson').get(0).reset();
           updateTable();
        }, handleError);
    });
});

function handleError(error) {
    toastr.success('Something bad happened');
    console.log(error);
};

function createPerson(person, successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/person/add',
        data: JSON.stringify(person),
        type: 'POST',
        dataType: 'json',
        success: successCallback,
        error: errorCallback
    });
}

function removePerson(person, successCallback, errorCallback) {
    $.ajax({
        contentType: 'application/json',
        url: '/api/person/delete',
        data: JSON.stringify(person),
        type: 'POST',
        success: successCallback,
        error: errorCallback
    });
}

function updateTable() {
    $('button.controls').prop('disabled', selectedId === undefined);
    $.get('/api/person/all', function(persons) {
        table.clear();
        table.rows.add(persons);
        table.columns.adjust().draw();
    });
}

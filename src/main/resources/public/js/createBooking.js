var guestsContainer;
var guestSelector;
var booking = {
    guests: [],
    rooms: []
};
var guests;

$('document').ready(function() {
    guestsContainer = $('#guestsContainer');
    guestSelector = $('#guestSelector');

    $('#addGuest').on('click', function() {
        $('#createGuestModal').modal('show');
    });

    $('#findGuest').on('click', function() {
        $('#findGuestModal').modal('show');
        setTimeout(function() {
            guestSelector.chosen();
        }, 200);
        ajaxJsonCall('GET', '/api/guests/', null, function(guests) {
            _.map(guests, function(guest) {
                var option = $('<option value="' + guest.id + '">' + guest.firstName + ' ' + guest.lastName + ' (' + guest.email + ')</option>');
                guestSelector.html();
                guestSelector.append(option);
                guestSelector.trigger("chosen:updated");
            });
        }, handleError);
    });

    $('#guestForm').submit(function(event) {
        event.preventDefault();
        var guest = {
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
            email: $("#email").val(),
            telephoneNumber: $("#telephoneNumber").val()
        };
        ajaxJsonCall('POST', '/api/guests/create', guest, function(result) {
            $('#guestModal').modal('hide');
            toastr.success('Added "' + guest.firstName + ' ' + guest.lastName + '" to Guests!');
            $('#guestForm').get(0).reset();
            guestsContainer.append(createGuestHtml(result));
        }, handleError);
    });
});

function createGuestHtml(guest) {
    var card = $('<div class="card col-6"></div>');
    var cardBody = $('<div class="card-body"></div>');
    var cardTitle = $('<h5 class="card-title"></div>');
    cardTitle.append(guest.firstName + ' ' + guest.lastName);
    var cardSubtitle = $('<h6 class="card-subtitle mb-2 text-muted"></h6>');
    cardSubtitle.append(guest.email);
    cardBody.append(cardTitle);
    cardBody.append(cardSubtitle);
    card.append(cardBody);
    return card;
}


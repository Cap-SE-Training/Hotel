function ajaxJsonCall(method, url, data, successCallback, errorCallback) {
    var options = {
        contentType: 'application/json',
        type: method,
        url: url,
        success: successCallback,
        error: errorCallback
    };
    if (data) {
        options.data = JSON.stringify(data);
    }
    $.ajax(options);
}

function handleError(error) {
    toastr.error(JSON.parse(error.responseText).message);
    console.log(error);
};
$(document).ready(function() {
    var context = $('#roomsChart');
    var roomsChart = new Chart(context, {
        type:'pie',
        data: {
            labels: ["Available", "Taken", "Blocked"], //is hardcoded but needs to be dynamic (retrieve it from enum)
            datasets: [{
                label:"Amount of rooms",
                data: [10, 5, 3], //getRooms()
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });
});

//function getRooms(){
//    ajaxJsonCall('GET', '/api/rooms/search', null, function{
//        //filter list
//    }, null);
//    }
//}
var map;
var marker;

function initMap() {
    var initialLocation;
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
            resume();
        });
    } else {
        initialLocation = new google.maps.LatLng(50, 0);
        resume();
    }

    function resume() {
        map = new google.maps.Map(document.getElementById('map'), {
            center: initialLocation,
            streetViewControl: false,
            zoom: 8
        });

        marker = new google.maps.Marker({
            position: initialLocation,
            map: map,
            draggable: true
        });

        google.maps.event.addListener(map, 'click', function(event) {
            placeMarker(event.latLng);
        });

        google.maps.event.addListener(marker, 'dragend', function(event) {
            placeMarker(event.latLng);
        })
    }

    function placeMarker(location) {
        console.log(location);
        marker.setPosition(location);
        //TODO send request with new location, include id's
    }
}
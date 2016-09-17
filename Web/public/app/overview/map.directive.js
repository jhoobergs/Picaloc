;(function() {
    'use strict';

    angular.module('picaloc')
        .directive('map', [mapDir]);

    function mapDir() {
        return {
            restrict: 'EA',
            scope: {location: '=location'},
            templateUrl: 'app/overview/map.template.html',
            link: link,
            controller: ['$scope', '$element', 'uiGmapGoogleMapApi', 'locationFactory', controller]
        };

        function link() {

        }
        
        function controller($scope, $element, uiGmapGoogleMapApi, locationFactory) {
            $scope.map = undefined;
            $scope.marker = undefined;

            var googleLoc = undefined;

            uiGmapGoogleMapApi.then(function(maps) {
                    console.log(maps);
                    var loc = locationFactory.getLocation();
                    googleLoc = new maps.LatLng(loc.latitude, loc.longitude);
                    $scope.map = new maps.Map(document.getElementById('map'), {
                        center: googleLoc,
                        streetViewControl: false,
                        zoom: 8
                    });

                    var input = document.getElementById('pac-input');
                    var searchBox = new maps.places.SearchBox(input);
                    $scope.map.controls[maps.ControlPosition.TOP_LEFT].push(input);
                    $('#pac-input').css('display', 'block');

                    // Bias the SearchBox results towards current map's viewport.
                    $scope.map.addListener('bounds_changed', function() {
                        searchBox.setBounds($scope.map.getBounds());
                    });

                    searchBox.addListener('places_changed', function() {
                        var places = searchBox.getPlaces();

                        if (places.length == 0) {
                            return;
                        }
                    });

                    $scope.marker = new maps.Marker({
                        position: googleLoc,
                        map: $scope.map,
                        draggable: true
                    });

                    maps.event.addListener($scope.map, 'click', function(event) {
                        placeMarker(event.latLng);
                    });

                    maps.event.addListener($scope.marker, 'dragend', function(event) {
                        placeMarker(event.latLng);
                    });

                function placeMarker(location) {
                    console.log(location);
                    $scope.marker.setPosition(location);
                    locationFactory.setLocation(location.lat(), location.lng());
                    //TODO send request with new location, include id's
                }
            });
        }
    }
})();
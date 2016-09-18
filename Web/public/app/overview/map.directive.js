;(function() {
    'use strict';

    angular.module('picaloc')
        .directive('map', [mapDir]);

    function mapDir() {
        return {
            restrict: 'EA',
            scope: {
                location: '=location',
                thumbnails: '=thumbnails'
            },
            templateUrl: 'app/overview/map.template.html',
            link: link,
            controller: ['$rootScope', '$scope', '$element', 'uiGmapGoogleMapApi', 'locationFactory', controller]
        };

        function link() {

        }
        
        function controller($rootScope, $scope, $element, uiGmapGoogleMapApi, locationFactory) {
            $scope.map = undefined;
            $scope.marker = undefined;
            $scope.nails = [];

            var googleLoc = undefined;

            uiGmapGoogleMapApi.then(function(maps) {
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

                // Listen for the event fired when the user selects a prediction and retrieve
                // more details for that place.
                searchBox.addListener('places_changed', function() {
                    var places = searchBox.getPlaces();

                    if (places.length == 0) {
                        return;
                    }

                    // For each place, get the icon, name and location.
                    var bounds = new google.maps.LatLngBounds();
                    var done = false;
                    places.forEach(function(place) {
                        if (!place.geometry) {
                            console.log("Returned place contains no geometry");
                            return;
                        }

                        if (done) {
                            return
                        }

                        // Create a marker for each place.
                        placeMarker(place.geometry.location);
                        done = true;

                        if (place.geometry.viewport) {
                            // Only geocodes have viewport.
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                    });
                    $scope.map.fitBounds(bounds);
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
                    $scope.marker.setPosition(location);
                    locationFactory.setLocation(location.lat(), location.lng());
                    $rootScope.$broadcast('markerChanged');
                }

                $scope.$on('locationChange', function(event, params) {
                    placeMarker(new maps.LatLng(params.latitude, params.longitude));
                    $scope.map.setCenter(new maps.LatLng(params.latitude, params.longitude));
                });

                $scope.$on('hover', function(event, param1, param2) {
                    if (param1 === 'end') {
                        $scope.nails.forEach(function(nail) {
                            nail.setOpacity(1);
                        })
                    } else {
                        $scope.nails.forEach(function(nail) {
                            if (nail.id !== param2.id) {
                                nail.setOpacity(0);
                            }
                        })
                    }
                });

                $scope.$watchCollection('thumbnails', function(newValue, oldValue) {
                    if (newValue) {
                        for (var j=0; j < $scope.nails.length; j++) {
                            $scope.nails[j].setMap(null);
                        }
                        $scope.nails = [];

                        for (var i=0; i < newValue.length; i++) {
                            var marker = new google.maps.Marker({
                                position: new maps.LatLng(newValue[i].latitude, newValue[i].longitude),
                                map: $scope.map,
                                id: newValue[i].id,
                                icon: {
                                    path: maps.SymbolPath.CIRCLE,
                                    scale: 5,
                                    fillColor: '#b92c28',
                                    strokeColor: '#b92c28'
                                }
                            });
                            $scope.nails.push(marker);
                        }
                    }
                });
            });
        }
    }
})();
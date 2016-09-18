;(function() {
    'use strict';

    angular.module('picaloc')
        .controller('overviewController', ['$scope', '$q', '$http', '$modal', '$filter', 'locationFactory', overviewCtrl]);

    function overviewCtrl($scope, $q, $http, $modal, $filter, locationFactory) {

        prepareView();

        function prepareView() {
            getThumbList().then(function success(response) {
                for (var i=0; i < response.length; i++) {
                    response[i].random = Math.random();

                    if (!response[i].likes_count) {
                        response[i].likes_count = 0;
                    }

                    if (response[i].image_url_id === "") {
                        response[i].image_url = 'image.png';
                    } else {
                        response[i].image_url = 'https://firebasestorage.googleapis.com/v0/b/picaloc-2acb6.appspot.com' + response[i].image_url_id;
                    }

                    if (response[i].title.length > 20) {
                        response[i].title_short = response[i].title.slice(0, 19).trim() + '...';
                    } else {
                        response[i].title_short = response[i].title;
                    }
                }

                switch ($scope.$parent.sort) {
                    case 'random':
                        $scope.thumbs = $filter('orderBy')(response, 'random');
                        break;
                    case 'top':
                        $scope.thumbs = $filter('orderBy')(response, 'likes_count', true);
                        break;
                    case 'recent':
                        $scope.thumbs = $filter('orderBy')(response, 'created_at', true);
                        break;
                }
            });
        }

        $scope.$on('sort', function() {
            prepareView();
        });

        $scope.$on('markerChanged', function(event) {
            prepareView();
        });

        function getThumbList() {
            var deferred = $q.defer();

            $http({
                method: 'POST',
                url: 'https://picaloc.herokuapp.com/posts/get',
                params: {
                    user_id: 1,
                    location: locationFactory.getLocation
                }
            }).then(function success(response) {
                deferred.resolve(response.data);
            });

            return deferred.promise;
        }

        var modal = $modal({scope: $scope, templateUrl: 'app/overview/thumbnailModal.template.html', show: false});
        // var modal = $modal({scope: $scope, content: 'app/overview/thumbnailModal.template.html', show: false});

        $scope.openModal = function(thumbnail) {
            $scope.modalInfo = {
                title: thumbnail.title,
                url: thumbnail.image_url,
                created: new Date(thumbnail.created_at).toDateString(),
                likes: thumbnail.likes_count
            };

            modal.$promise.then(modal.show);
        };

        $scope.hideModal = function() {
            modal.$promise.then(modal.hide);
        };

        $scope.startHover = function(thumbnail) {
            $scope.$broadcast('hover', 'start', thumbnail);
        };

        $scope.endHover = function(thumbnail) {
            $scope.$broadcast('hover', 'end', thumbnail);
        };
    }
})();
;(function() {
    'use strict';

    angular.module('picaloc')
        .controller('overviewController', ['$scope', 'thumbnailsFactory', overviewCtrl]);

    function overviewCtrl($scope, thumbnailsFactory) {
        thumbnailsFactory.getThumbList().then(function success(response) {
            $scope.thumbs = response;
        });
    }
})();
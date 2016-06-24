(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Region'];

    function RegionDetailController($scope, $rootScope, $stateParams, entity, Region) {
        var vm = this;

        vm.region = entity;

        var unsubscribe = $rootScope.$on('couponkicksvcApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('AddressDetailController', AddressDetailController);

    AddressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Address', 'Country'];

    function AddressDetailController($scope, $rootScope, $stateParams, entity, Address, Country) {
        var vm = this;

        vm.address = entity;

        var unsubscribe = $rootScope.$on('couponkicksvcApp:addressUpdate', function(event, result) {
            vm.address = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

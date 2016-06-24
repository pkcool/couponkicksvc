(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('MerchantDetailController', MerchantDetailController);

    MerchantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Merchant', 'Address'];

    function MerchantDetailController($scope, $rootScope, $stateParams, entity, Merchant, Address) {
        var vm = this;

        vm.merchant = entity;

        var unsubscribe = $rootScope.$on('couponkicksvcApp:merchantUpdate', function(event, result) {
            vm.merchant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

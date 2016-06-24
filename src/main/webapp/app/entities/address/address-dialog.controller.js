(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('AddressDialogController', AddressDialogController);

    AddressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Address', 'Country'];

    function AddressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Address, Country) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.save = save;
        vm.countries = Country.query({filter: 'address-is-null'});
        $q.all([vm.address.$promise, vm.countries.$promise]).then(function() {
            if (!vm.address.country || !vm.address.country.id) {
                return $q.reject();
            }
            return Country.get({id : vm.address.country.id}).$promise;
        }).then(function(country) {
            vm.countries.push(country);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.address.id !== null) {
                Address.update(vm.address, onSaveSuccess, onSaveError);
            } else {
                Address.save(vm.address, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('couponkicksvcApp:addressUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

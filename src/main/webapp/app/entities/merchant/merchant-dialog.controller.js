(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('MerchantDialogController', MerchantDialogController);

    MerchantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Merchant', 'Address'];

    function MerchantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Merchant, Address) {
        var vm = this;

        vm.merchant = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.billingaddresses = Address.query({filter: 'merchant-is-null'});
        $q.all([vm.merchant.$promise, vm.billingaddresses.$promise]).then(function() {
            if (!vm.merchant.billingAddressId) {
                return $q.reject();
            }
            return Address.get({id : vm.merchant.billingAddressId}).$promise;
        }).then(function(billingAddress) {
            vm.billingaddresses.push(billingAddress);
        });
        vm.shippingaddresses = Address.query({filter: 'merchant-is-null'});
        $q.all([vm.merchant.$promise, vm.shippingaddresses.$promise]).then(function() {
            if (!vm.merchant.shippingAddressId) {
                return $q.reject();
            }
            return Address.get({id : vm.merchant.shippingAddressId}).$promise;
        }).then(function(shippingAddress) {
            vm.shippingaddresses.push(shippingAddress);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.merchant.id !== null) {
                Merchant.update(vm.merchant, onSaveSuccess, onSaveError);
            } else {
                Merchant.save(vm.merchant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('couponkicksvcApp:merchantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.modifiedDate = false;
        vm.datePickerOpenStatus.lastLogonDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

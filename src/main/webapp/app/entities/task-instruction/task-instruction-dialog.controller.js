(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('TaskInstructionDialogController', TaskInstructionDialogController);

    TaskInstructionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TaskInstruction'];

    function TaskInstructionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TaskInstruction) {
        var vm = this;

        vm.taskInstruction = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taskInstruction.id !== null) {
                TaskInstruction.update(vm.taskInstruction, onSaveSuccess, onSaveError);
            } else {
                TaskInstruction.save(vm.taskInstruction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('couponkicksvcApp:taskInstructionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

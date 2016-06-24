(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('TaskInstructionDeleteController',TaskInstructionDeleteController);

    TaskInstructionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TaskInstruction'];

    function TaskInstructionDeleteController($uibModalInstance, entity, TaskInstruction) {
        var vm = this;

        vm.taskInstruction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TaskInstruction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

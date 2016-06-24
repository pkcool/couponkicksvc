(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('TaskInstructionDetailController', TaskInstructionDetailController);

    TaskInstructionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TaskInstruction'];

    function TaskInstructionDetailController($scope, $rootScope, $stateParams, entity, TaskInstruction) {
        var vm = this;

        vm.taskInstruction = entity;

        var unsubscribe = $rootScope.$on('couponkicksvcApp:taskInstructionUpdate', function(event, result) {
            vm.taskInstruction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

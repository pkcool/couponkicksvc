(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('TaskInstructionController', TaskInstructionController);

    TaskInstructionController.$inject = ['$scope', '$state', 'TaskInstruction', 'TaskInstructionSearch'];

    function TaskInstructionController ($scope, $state, TaskInstruction, TaskInstructionSearch) {
        var vm = this;
        
        vm.taskInstructions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            TaskInstruction.query(function(result) {
                vm.taskInstructions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TaskInstructionSearch.query({query: vm.searchQuery}, function(result) {
                vm.taskInstructions = result;
            });
        }    }
})();

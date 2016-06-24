(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Task'];

    function TaskDetailController($scope, $rootScope, $stateParams, entity, Task) {
        var vm = this;

        vm.task = entity;

        var unsubscribe = $rootScope.$on('couponkicksvcApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

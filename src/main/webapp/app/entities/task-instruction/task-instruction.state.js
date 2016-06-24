(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-instruction', {
            parent: 'entity',
            url: '/task-instruction',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'couponkicksvcApp.taskInstruction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-instruction/task-instructions.html',
                    controller: 'TaskInstructionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskInstruction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-instruction-detail', {
            parent: 'entity',
            url: '/task-instruction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'couponkicksvcApp.taskInstruction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-instruction/task-instruction-detail.html',
                    controller: 'TaskInstructionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskInstruction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TaskInstruction', function($stateParams, TaskInstruction) {
                    return TaskInstruction.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('task-instruction.new', {
            parent: 'task-instruction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-instruction/task-instruction-dialog.html',
                    controller: 'TaskInstructionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                taskInstructionId: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task-instruction', null, { reload: true });
                }, function() {
                    $state.go('task-instruction');
                });
            }]
        })
        .state('task-instruction.edit', {
            parent: 'task-instruction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-instruction/task-instruction-dialog.html',
                    controller: 'TaskInstructionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskInstruction', function(TaskInstruction) {
                            return TaskInstruction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-instruction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-instruction.delete', {
            parent: 'task-instruction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-instruction/task-instruction-delete-dialog.html',
                    controller: 'TaskInstructionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TaskInstruction', function(TaskInstruction) {
                            return TaskInstruction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-instruction', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

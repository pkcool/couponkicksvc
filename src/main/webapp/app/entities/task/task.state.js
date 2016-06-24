(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task', {
            parent: 'entity',
            url: '/task?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'couponkicksvcApp.task.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task/tasks.html',
                    controller: 'TaskController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('task');
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-detail', {
            parent: 'entity',
            url: '/task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'couponkicksvcApp.task.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task/task-detail.html',
                    controller: 'TaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('task');
                    $translatePartialLoader.addPart('language');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Task', function($stateParams, Task) {
                    return Task.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('task.new', {
            parent: 'task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-dialog.html',
                    controller: 'TaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                taskId: null,
                                title: null,
                                description: null,
                                startDate: null,
                                endDate: null,
                                language: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('task');
                });
            }]
        })
        .state('task.edit', {
            parent: 'task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-dialog.html',
                    controller: 'TaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task.delete', {
            parent: 'task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-delete-dialog.html',
                    controller: 'TaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Task', function(Task) {
                            return Task.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

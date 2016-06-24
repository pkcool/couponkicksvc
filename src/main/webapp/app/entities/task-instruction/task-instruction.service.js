(function() {
    'use strict';
    angular
        .module('couponkicksvcApp')
        .factory('TaskInstruction', TaskInstruction);

    TaskInstruction.$inject = ['$resource'];

    function TaskInstruction ($resource) {
        var resourceUrl =  'api/task-instructions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

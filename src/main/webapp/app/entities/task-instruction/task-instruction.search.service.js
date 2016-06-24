(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .factory('TaskInstructionSearch', TaskInstructionSearch);

    TaskInstructionSearch.$inject = ['$resource'];

    function TaskInstructionSearch($resource) {
        var resourceUrl =  'api/_search/task-instructions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

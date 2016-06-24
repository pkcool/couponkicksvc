(function() {
    'use strict';
    angular
        .module('couponkicksvcApp')
        .factory('Merchant', Merchant);

    Merchant.$inject = ['$resource', 'DateUtils'];

    function Merchant ($resource, DateUtils) {
        var resourceUrl =  'api/merchants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.modifiedDate = DateUtils.convertDateTimeFromServer(data.modifiedDate);
                        data.lastLogonDate = DateUtils.convertDateTimeFromServer(data.lastLogonDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

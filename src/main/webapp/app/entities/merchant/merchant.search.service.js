(function() {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .factory('MerchantSearch', MerchantSearch);

    MerchantSearch.$inject = ['$resource'];

    function MerchantSearch($resource) {
        var resourceUrl =  'api/_search/merchants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

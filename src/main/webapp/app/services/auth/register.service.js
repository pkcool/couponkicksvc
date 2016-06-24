(function () {
    'use strict';

    angular
        .module('couponkicksvcApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();

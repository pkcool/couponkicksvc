'use strict';

describe('Controller Tests', function() {

    describe('TaskInstruction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTaskInstruction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTaskInstruction = jasmine.createSpy('MockTaskInstruction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TaskInstruction': MockTaskInstruction
            };
            createController = function() {
                $injector.get('$controller')("TaskInstructionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'couponkicksvcApp:taskInstructionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

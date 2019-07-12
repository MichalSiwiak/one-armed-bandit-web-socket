var functions = angular.module("SessionConfig", []);
functions.controller("SessionConfigController", function ($scope, $http) {

    $scope.games = [];
    var socket = null;
    var stompClient = null;

    getAllGames();

    function getAllGames() {
        $http({
            method: 'GET',
            url: 'sessions'
        }).then(function successCallback(response) {
            $scope.games = response.data;
            angular.forEach($scope.games, function (value, index) {
                if (value.status != 'CLOSED') {
                    socket = new SockJS('/one-armed-bandit-websocket');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function (frame) {
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/game/end-game/' + value.gameId, function (message) {
                        });
                    });
                }
            });
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.closeGame = function (id, token) {
        $http({
            method: 'POST',
            url: 'sessions/' + id
        }).then(function successCallback(response) {
            stompClient.send("/app/end/" + id, {}, JSON.stringify({'authorizationToken': token}));
            getAllGames();
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    };

});
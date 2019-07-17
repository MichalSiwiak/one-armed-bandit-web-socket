var functions = angular.module("SessionConfig", []);
functions.controller("SessionConfigController", function ($scope, $http, $timeout) {

    $scope.games = [];
    var socket = null;
    var stompClient = null;

    getAllGames();
    connect();

    function connect() {
        socket = new SockJS('/one-armed-bandit-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/game/results-game', function () {
                getAllGames();
            });
        });
    }

    function getAllGames() {
        $http({
            method: 'GET',
            url: 'sessions'
        }).then(function successCallback(response) {
            $scope.games = response.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.closeGame = function (id, token) {
        stompClient.send("/app/end/" + id, {}, JSON.stringify({'authorizationToken': token, 'gameId': id}));
        $timeout(function () {
            stompClient.send("/app/results", {}, JSON.stringify({'gameId': id}));
        }, 500);
    };

    /*function updateGame() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        getAllGames();
    }*/

});
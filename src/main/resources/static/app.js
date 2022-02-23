let stompClient;

const connect = () => {
    let sockJS = new SockJS("http://localhost:8080/ws");
    stompClient = Stomp.over(sockJS);
    stompClient.connect({}, onConnected, onError);
};

const onConnected = () => {
    console.log("connected");

    stompClient.subscribe(
        "/topic/game.1",
        onMessageReceived
    );
};

const onError = (msg) => {
    console.log(msg);
};

const onMessageReceived = (msg) => {
    console.log(msg);
};

const joinGame = (username) => {
    if (username.trim() !== "") {
        const message = {
            message: username
        };

        stompClient.send("/app/game.1.join", {}, JSON.stringify(message));
    }
};

const sendMessage = (msg) => {
    if (msg.trim() !== "") {
        const message = {
            message: msg
        };

        stompClient.send("/app/game.1", {}, JSON.stringify(message));
    }
};

connect();
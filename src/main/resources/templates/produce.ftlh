<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Produce</title>
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script>
        var webSocket;

        function connect() {
            webSocket = new WebSocket('ws://localhost:8080/jlmq');
        }

        function sendMessage(queueName, email, username, realName) {
            let payload = {
                "command": "send",
                "queueName": queueName,
                "body": {
                    "email": email,
                    "userData": {
                        "username": username,
                        "realname": realName
                    }
                }
            };

            let message = {
                "header": "send",
                "payload": payload
            };

            webSocket.send(JSON.stringify(message));
        }
    </script>
</head>
<body onload="connect()">
<div>
    <label for="message">Task data</label>
    <br>
    <input type="text" id="queueName" placeholder="queueName">
    <br>
    <input type="text" id="email" placeholder="email">
    <br>
    <input type="text" id="username" placeholder="username">
    <br>
    <input type="text" id="realname" placeholder="realname">

    <button onclick="sendMessage($('#queueName').val(), $('#email').val(), $('#username').val(), $('#realname').val(),)">Send</button>

</div>
</body>
</html>
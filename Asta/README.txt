
Effettuare multiplexing di porte diverse a livello di pacchetto,
non allocare più server su più porte ma un solo server su una sola
porta

Disaccoppiare il canale dal client e dal server: un TCPChannel
e un WebSocketChannel, l'uno che mandi dati su un socket e l'altro
su un WebSocket.


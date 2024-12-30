import React, { useEffect, useState } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import WebSocketService from '../API/socketService';


interface Notification {
  date: string;
  message: string;
  link:string;
}

interface NotificationConsumerProps {
  userId: number;
}

const NotificationConsumer: React.FC<NotificationConsumerProps> = ({ userId }) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const webSocketService = new WebSocketService("ws://localhost:8080/ws");

  useEffect(() => {
    
    const onConnect = () => {
      console.log('Connected to WebSocket');

      webSocketService.subscribe(`/topic/notifications/${userId}`, (message) => {
        console.log(JSON.parse(message.body));
        setNotifications((prev) => [...prev, JSON.parse(message.body)])
      });
      
    }
    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };
    webSocketService.connect(onConnect, onError, userId);
    return () => {
      webSocketService.disconnect();
    };
  }, [userId]);

  return (
    <div>
      <h2>Notifications</h2>
      <ul>
        {notifications.map((notif, index) => (
          <li key={index}>{notif.message}</li>
        ))}
      </ul>
    </div>
  );
};

export default NotificationConsumer;
